(defproject scr "0.1.0-SNAPSHOT"
  :description "Naver Dictionary Scroller"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [http-kit "2.1.18"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-json "0.4.0"]                 
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [org.jsoup/jsoup "1.8.3"]
                 [com.h2database/h2 "1.3.176"]
                 [org.slf4j/slf4j-log4j12 "1.7.13"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler scr.core/app
         :init scr.sql/init }
  :uberjar-name "start-0.1.4.jar"
  :profiles {:uberjar {:aot :all}}
  :main scr.core)
