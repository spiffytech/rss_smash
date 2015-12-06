(ns rss-smash.core
  (:gen-class)
  (:require [clojure.pprint :refer [pprint]]
            [http.async.client :as http]
            [byte-streams :as bs]
            [feedparser-clj.core :as fp]))

(def feeds ["http://spiffy.tech/tv_torrents.rss"])

(defn fetch-feed-body [url]
  (with-open [client (http/create-client)]
    (->
     (http/GET client url)
     (http/await)
     (http/string))))

(defn -main
  [& args]
  (->>
   feeds
   (map fetch-feed-body)
   (map bs/to-input-stream)
   (map fp/parse-feed)
   (map :entries)
   (pprint)
  ))
