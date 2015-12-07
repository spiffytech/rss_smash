(ns rss-smash.core
  (:gen-class)
  (:require [clojure.pprint :refer [pprint]]
            [http.async.client :as http]
            [clojure.set :refer [rename-keys]]
            [byte-streams :as bs]
            [feedparser-clj.core :as fp]
            [clj-rss.core :as rss]))

(def feeds ["http://spiffy.tech/tv_torrents.rss"
            "http://showrss.info/rss.php?user_id=248483&hd=null&proper=null"])

(defn fetch-feed-body [url]
  (with-open [client (http/create-client)]
    (->
     (http/GET client url)
     (http/await)
     (http/string))))

(defn munge-tags
  "feedparser doesn't honor zero-or-one tag restrictions. Fix tags so clj-rss will accept them."
  [item]
  (let [blacklisted-keys [:contributors :contents]
        key-instructions [{:bad :categories :good :category :fn (partial into [])}
                          {:bad :authors :good :author :fn first}
                          {:bad :enclosures :good :enclosure :fn #(vector (dissoc (into {} (first %)) :uri))}
                          {:bad :published-date :good :pubDate :fn identity}
                          {:bad :uri :good :guid :fn #(vector {:isPermaLink false} %)}
                          {:bad :description :good :description :fn #(:value %)}]
        key-replacements (into {}
                               (map
                                #(vector (:bad %) (:good %))
                                key-instructions))
        singularize-values (fn [item rule]
                             (let [k (:good rule)
                                   v (k item)
                                   f (:fn rule)]
                               (if v
                                 (assoc item k (f v))
                                 item
                                 )
                               ))
        purge-nils (fn purge-nils' [acc [k v]]
                     (if (nil? v)
                       acc
                       ;(if (coll? v)
                       ;  (assoc acc k (reduce purge-nils' {} v))
                         (assoc acc k v)))]

    (as-> item $
      (apply dissoc $ blacklisted-keys)
      (rename-keys $ key-replacements)
      (reduce singularize-values $ key-instructions)
      (reduce purge-nils {} $))))

(defn mk-feed [items]
  (rss/channel-xml {  ; False means no content validation. clj-rss doesn't recognize all valid tags.
                :title "spiffytech's TV shows"
                :description "spiffytech's TV shows"
                :link "http://spiffy.tech/tv_torrents.rss"}
               items)

)

(defn -main
  [& args]
  (->>
   feeds
   (map fetch-feed-body)
   (map bs/to-input-stream)
   (map fp/parse-feed)  ; Is buggy with URLs, so we have to fetch them ourselves
   (mapcat :entries)
   (map (partial into {}))  ; feedparser returns structs
   (map munge-tags)
   (sort-by :pubDate)
   ((fn [items] (println (count items)) items))
   (mk-feed)
   (spit "feed.xml")
  ))
