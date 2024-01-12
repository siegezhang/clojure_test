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

;; Sample map structure we want to destructure.
(def user {:first-name "Hubert"
           :last-name  "Klein Ikkink"
           :alias      "mrhaki"})

;; We can define a symbol username that will have the
;; the value of the :alias key of the user map.
(let [{username :alias} user]
  (is (= "mrhaki" username)))

;; When we use a non-existing key the symbol will
;; have a nil value, like the symbol city in the
;; following example.
(let [{username :alias city :city} user]
  (is (nil? city))
  (is (= "mrhaki" username)))

;; We can use :or to define a value when a key
;; is not available in the map.
;; Here we define "Tilburg" as default value if
;; the :city key is missing from the map.
(let [{username :alias city :city :or {city "Tilburg"}} user]
  (is (= "Tilburg" city))
  (is (= "mrhaki" username)))

;; The symbol names must match in the definition
;; for the key value and the :or value.
(let [{username :alias lives-in :city :or {lives-in "Tilburg"}} user]
  (is (= "Tilburg" lives-in))
  (is (= "mrhaki" username)))

;; We can use :as to assign the original map
;; to a symbol, that we can use in the code.
(let [{username :alias :as person} user]
  (is (= "Hubert" (:first-name person)))
  (is (= "Klein Ikkink" (:last-name person)))
  (is (= "mrhaki" username)))

;; If the symbol name matches the key name we
;; can use :keys to define that so we have to type less.
(let [{:keys [alias first-name last-name]} user]
  (is (= "mrhaki" alias))
  (is (= "Hubert" first-name))
  (is (= "Klein Ikkink" last-name)))

;; Combination of destruturing options for a map.
(let [{:keys [first-name last-name city]
       :or   {city "Tilburg"}
       :as   person} user]
  (is (= "Hubert" first-name))
  (is (= "Klein Ikkink" last-name))
  (is (= "Tilburg" city))
  (is (= "mrhaki" (:alias person))))


;; Use destructuring in a function argument.
(defn who-am-i
  [{:keys [first-name last-name city]
    :or   {city "Tilburg"}
    :as   person}]
  (str first-name " " last-name ", aka " (:alias person) ", lives in " city))

(is (= "Hubert Klein Ikkink, aka mrhaki, lives in Tilburg"
       (who-am-i user)))


;; Another map with string keys.
(def string-map {"alias" "mrhaki" "city" "Tilburg"})

(let [{username "alias" city "city"} string-map]
  (is (= "mrhaki" username))
  (is (= "Tilburg" city)))

;; We can use :strs instead of :keys for string keys.
(let [{:strs [alias city]} string-map]
  (is (= "mrhaki" alias))
  (is (= "Tilburg" city)))

;; Or convert string keys to keywords.
;(let [{:keys [alias city]} (keywordize-keys string-map)]
;  (is (= "mrhaki" alias))
;  (is (= "Tilburg" city)))


;; For completeness we can destructure symbol keys.
(def sym-map {'alias "mrhaki" 'name "Hubert Klein Ikkink"})

(let [{username 'alias} sym-map]
  (is (= "mrhaki" username)))

;; We can use :str instead of :keys.
(let [{:syms [alias name]} sym-map]
  (is (= "mrhaki" alias))
  (is (= "Hubert Klein Ikkink" name)))