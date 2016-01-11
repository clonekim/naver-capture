(ns scr.sql
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log])
  (:import (java.io File)))

(defonce dic-dir (str (or (System/getenv "H2_URL") (System/getProperty "user.home")) (File/separator) "db"))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname (str dic-dir (File/separator) "dic") })

(defn create-schema []
  (log/debug "run ddl -- create schema")
    (jdbc/db-do-commands db-spec 
                         (jdbc/create-table-ddl :batch_info
                                                [:id "int primary key auto_increment"]
                                                [:prime_id "int default 0"]
                                                [:url "varchar(150) unique"]
                                                [:letter "varchar(5)"]
                                                [:stat "varchar(20)"]
                                                [:created "datetime"]
                                                [:exception "varchar"])

                         (jdbc/create-table-ddl :hanja
                                                [:id "int primary key auto_increment"]
                                                [:batch_id "int"]
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
                                                [:hanja_id "int"]
                                                [:mean "varchar(255)"])))

(defn init []
  (log/debug "db path --" dic-dir)
  (log/debug "run ddl -- create table")
  (if (.exists (File. dic-dir))
    (log/debug "already database exists! -- skip")    
    (create-schema)))


(defn insert-hanja [kv]
  (log/debug "insert hanja -- " kv)
  (jdbc/with-db-transaction [tx db-spec]
    (let [ pk ((first (first  (jdbc/insert! tx :hanja (dissoc  kv :means)))) 1)]
      (doseq [mean (:means kv)]
        (jdbc/insert! tx :hanja_annotation {:hanja_id pk :mean (get mean "mean") })))))


(defmulti select-hanja class)

(defmethod select-hanja java.lang.String [consonant]
  (jdbc/query db-spec ["select * from hanja where consonant = ?" consonant]))

(defmethod select-hanja clojure.lang.PersistentVector [v]
  (jdbc/query db-spec ["select * from hanja where consonant = ? and pronun=?" (first v) (last v)]))

(defmethod select-hanja java.lang.Integer [id]
  (jdbc/query db-spec ["select * from hanja where id = ?" id]))


(defn select-hanja-pronun
  ([s]
   (jdbc/query db-spec ["select pronun from hanja where consonant = ? group by pronun order by pronun" s]))
  ([s0 s1]
   (jdbc/query db-spec ["select pronun from hanja where cosonant = ? and pronun = ? group by pronun order by pronun" s0 s1])))


(defn select-hanja-annotation
  ([]
   (jdbc/query db-spec ["select * from hanja_annotation"]))
  ([id]
   (jdbc/query db-spec ["select * from hanja_annotation where hanja_id =?" id])))

(defn total-hanja []
  (first (jdbc/query db-spec ["select count(id) as count from hanja"])))


(defn drop-schema []
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
                  {:stat (:stat kv) :exception (:exception kv)}
                  ["id = ?" (:batch-id kv)])))



(defn select-batch
  ([]
   (jdbc/query db-spec ["select * from batch_info"]))
  ([id]
   (jdbc/query db-spec ["select * from batch_info where id = ?" id])))
