(ns scr.sql
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log])
  (:import (java.io File)))

(defonce dic-dir (str (or (System/getProperty "user.home")
                          (System/getenv "H2_URL")) "/db"))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname (str dic-dir "/dic") })


(defn init []
  (log/debug "run ddl -- create table")
  (log/debug "it will create db --" dic-dir)
  (if (.exists (File. dic-dir))
    (log/debug "already databases exists! -- skip")    
    (jdbc/db-do-commands db-spec 
                         (jdbc/create-table-ddl :batch_info
                                                [:id "int primary key auto_increment"]
                                                [:url "varchar(150)"]
                                                [:stat "varchar(20)"]
                                                [:created "datetime"])

                         (jdbc/create-table-ddl :hanja
                                                [:id "int primary key auto_increment"]
                                                [:consonant "varchar(5)"]
                                                [:letter "varchar(10)"]
                                                [:theory "varchar"]
                                                [:pronun "varchar(100)"]
                                                [:read_pronun "varchar(100)"]
                                                [:bushou_letter "varchar(10)"]
                                                [:bushou_mean "varchar(300)"]
                                                [:bushou_name "varchar(100)"]
                                                [:bushou_pronun "varchar(100)"]
                                                [:bushou_stroke "tinyint"]
                                                [:total_stroke "tinyint"])

                         (jdbc/create-table-ddl :hanja_annotation
                                                [:id "int primary key auto_increment"]
                                                [:hanja_id "int"]
                                                [:mean "varchar(255)"]))))


(defn insert-hanja [kv]
  (log/debug "insert hanja -- " kv)
  (jdbc/with-db-transaction [tx db-spec]
    (let [ pk ((first (first  (jdbc/insert! tx :hanja (dissoc  kv :means)))) 1)]
      (doseq [mean (:means kv)]
        (jdbc/insert! tx :hanja_annotation {:hanja_id pk :mean (get mean "mean") })))))



(defn select-hanja []
  (jdbc/query db-spec ["select * from hanja"]))



(defn select-hanja-annotation
  ([]
   (jdbc/query db-spec ["select * from hanja_annotation"]))
  ([id]
   (jdbc/query db-spec ["select * from hanja_annotation where hanja_id =?" id])))



(defn shred []
  (log/debug "run ddl -- drop table")
  (jdbc/db-do-commands db-spec
                       (jdbc/drop-table-ddl :hanja)
                       (jdbc/drop-table-ddl :hanja_annotation)
                       (jdbc/drop-table-ddl :batch_info)))



(defn insert-batch [kv]
  (log/debug "insert batch -- " kv)
  (jdbc/with-db-transaction [tx db-spec]
    ((first (first (jdbc/insert! tx :batch_info kv))) 1)))



(defn update-batch [kv]
  (log/debug "update batch -- " kv)
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/update! tx :batch_info
                  {:stat (:stat kv)}
                  ["id = ?" (:batch-id kv)])))



(defn select-batch
  ([]
   (jdbc/query db-spec ["select * from batch_info"]))
  ([id]
   (jdbc/query db-spec ["select * from batch_info where id = ?" id])))
