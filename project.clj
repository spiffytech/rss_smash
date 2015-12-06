(defproject rss_smash "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [http.async.client "1.1.0"]
                 [byte-streams "0.2.0"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [clj-rss "0.2.2"]]
  :main ^:skip-aot rss-smash.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
