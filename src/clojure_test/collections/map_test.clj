(ns clojure_test.collections.map_test
  (:require [clojure.test :refer [is]]))

;; Merge maps and use the function specified as first argument
;; to calculate the value for keys that are present
;; in multiple maps.
(is (= {:a 60 :b 3 :c 44 :d 100} (merge-with * {:a 2 :b 3 :c 4} {:a 10 :c 11} {:a 3 :d 100})))

;; Works for all maps and independent of type that is used for keys.
;; We can use any function for merge-with.
(def languages (merge-with (comp vec flatten conj) {"Clojure" [:dynamic :functional]}
                           {"Java" [:jvm]}
                           {"Groovy" [:jvm]}
                           {"Clojure" [:jvm]}
                           {"Groovy" [:dynamic]}))

(is (= {"Clojure" [:dynamic :functional :jvm]
        "Java"    [:jvm]
        "Groovy"  [:jvm :dynamic]}
       languages))


;; Sample map with small inventory.
(def inventory {"pencil" {:count 10 :price 0.25}
                "pen"    {:count 23 :price 0.4}})
;; Sample basket with items.
(def basket {"pencil" {:count 5} "pen" {:count 2}})

;; Function to subtract the :count value for a basket item
;; from the :count value for the same inventory item.
(defn item-sold
  [inventory-item basket-item]
  (update-in inventory-item [:count] - (:count basket-item)))

(is (= {"pencil" {:count 5 :price 0.25}
        "pen"    {:count 21 :price 0.4}}
       (merge-with item-sold inventory basket)))