(ns scr.core
  (:use ring.util.response)
  (:import (org.jsoup Jsoup)
           (java.net URLEncoder))
  (:require [compojure.core :refer :all]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.json :refer [wrap-json-response
                                          wrap-json-body
                                          wrap-json-params]]
            [org.httpkit.server :refer [run-server]]
            [clojure.core.async :as async]
            [clojure.tools.logging :as log]
            [hiccup.page :as page]
            [scr.sql :as sql]
            [cheshire.core :refer [parse-string]])
  (:gen-class))

(defonce tout (* 1000 20))

(defn get-doc
  ([url]
   (.get 
    (.timeout 
     (Jsoup/connect url) tout))))

(defn urlparam-encode [q]
  (URLEncoder/encode q "utf-8"))

(defn fetch
  ([url]
   (log/debug "fetching -- " url)
   (into [] (for [tree (.select (get-doc url) ".bordtop_bl a")] (.text tree))))
  ([url q]
   (log/debug "fetching -- " url q)
   (parse-string (.text (get-doc (str url (urlparam-encode q)))))))


(defn parse-item [el]
  (assoc {}
         :letter (get el "letter")
         :pronun (get el "letterPronun")
         :read_pronun (get el "readPronun")
         :theory (get el "theoryDescription")
         :bushou_letter (get-in el ["bushou" "bushouLetter"])
         :bushou_name (get-in el ["bushou" "bushouName"])
         :bushou_mean (get-in el ["bushou" "bushouMean"])
         :bushou_pronun (get-in el ["bushou" "bushouPronun"])
         :bushou_stroke (get-in el ["bushou" "bushouStroke"])
         :total_stroke (get el "totalStrokeCount")             
         :means  (get el "letterMeans")))


(defn go-chan [v kv]
  (let [http-chan (async/chan 10)]
    (doseq [i v]
      (async/go 
        (async/>!! http-chan 
                   (let [url "http://hanja.naver.com/tooltip.nhn?q="
                         q (str (.charAt i 0))
                         url-en (str url (urlparam-encode q))
                         batch-id (sql/insert-batch {:url url-en :stat "START" :created (java.util.Date.)})]
                     (merge (assoc kv :batch-id batch-id)
                            (parse-item (fetch url q))))))
      (async/go 
        (do 
          (let [m (async/<!! http-chan)]
            (sql/insert-hanja (dissoc m :batch-id))
            (sql/update-batch {:batch-id (:batch-id m) :stat "END" })))))))


(defn add-batch [url consonant]
  (let [pk (sql/insert-batch {:url url :stat "REGISTERED" :created (java.util.Date.)})]
    (log/debug "batch id -- " pk)
    (go-chan (fetch url) {:consonant consonant})
    pk))

(defroutes app-routes
  (GET "/" [] 
       (page/html5 {:ng-app "app"}
                   [:head
                    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
                    [:title "== SCR =="]
                    (page/include-css "css/bootstrap.min.css")]
                   [:body
                    [:ng-view]
                    (page/include-js "js/app.js")]))

  (GET "/api/dic" []
       (log/debug "query dictionary --")
       (sql/select-hanja))

  (GET "/api/dic-annot" [id]
       (sql/select-hanja-annotation id))

  (GET "/api/batch-stat" []
       (sql/select-batch))
  
  (GET "/api/batch-stat/:id" [id]
       (sql/select-batch id))

  (POST "/api/batch" [url consonant]
        (response {:batch_id (add-batch url consonant)}))

  (POST "/api/reset" []
        (response {:stat (sql/shred)})))


(def app
  (-> app-routes
      wrap-json-body
      wrap-json-params
      wrap-json-response
      (wrap-resource "public")))

(defn -main []
  (log/info "listen on port 5678")
  (run-server #'app {:port 5678}))
