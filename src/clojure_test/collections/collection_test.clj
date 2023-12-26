(ns clojure_test.collections.collection_test
  (:require [clojure.set :refer [map-invert]]
            [clojure.test :refer [is]]
            ))

;; Sample vector with some JVM langauges.
(def languages ["Clojure" "Groovy" "Java"])

;; Sample map describing a user.
(def user {:alias "mrhaki" :first "Hubert" :last "Klein Ikkink" :country "The Netherlands"})

;; Using butlast to get all elements but
;; not the last element as a sequence.
(is (= '("Clojure" "Groovy") (butlast languages)))

;; We can also use butlast on a map, the result
;; is a sequence with vectors containing the
;; key/value pairs from the original map.
(is (= '([:alias "mrhaki"] [:first "Hubert"] [:last "Klein Ikkink"])
       (butlast user))
    ;; We can use the into function to transform this
    ;; into a map again.
    (= {:alias "mrhaki" :first "Hubert" :last "Klein Ikkink"}
       (into {} (butlast user))))

;; Returns nil when collection is empty.
(is (= nil (butlast [])))


;; drop-last returns a lazy sequence with all
;; elements but the last element.
(is (= '("Clojure" "Groovy") (drop-last languages)))

;; Returns an empty sequence when collection is empty.
(is (= '() (drop-last [])))

;; We can use an extra argument with but-last
;; to indicate the number of items to drop
;; from the end of the collection.
;; butlast cannot do this.
(is (= ["Clojure"] (drop-last 2 languages)))

;; drop-last works on maps just like butlast.
(is (= '([:alias "mrhaki"]) (drop-last 3 user))
    (= {:alias "mrhaki"} (into {} (drop-last 3 user))))


(is (= {"mrhaki" :alias "Clojure" :language}
       (map-invert {:alias "mrhaki" :language "Clojure"})))

;; With duplicate values only one will be key.
(is (= {1 :c 2 :b 3 :d}
       (map-invert {:a 1 :b 2 :c 1 :d 3})))