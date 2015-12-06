(ns rss-smash.core
  (:gen-class)
  (:require [clojure.pprint :refer [pprint]]
            [http.async.client :as http]
            [byte-streams :as bs]
            [feedparser-clj.core :as fp]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (with-open [client (http/create-client)]
  (let [body (->
              (http/GET client "http://spiffy.tech/tv_torrents.rss")
              (http/await)
              (http/string)
              (bs/to-input-stream)
              )
        entries (:entries (fp/parse-feed body))]
  (pprint entries))))
  ;(pprint (fp/parse-feed "http://spiffy.tech/tv_torrents.rss")))))
