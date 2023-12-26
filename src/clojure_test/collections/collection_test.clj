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


;; In the following example we have the results
;; from several throws with a dice and we want
;; to remove all duplicates.
(is (= [1 5 6 2 3] (distinct [1 5 5 6 2 3 3 1])))

;; Only duplicates are removed.
(is (= ["Clojure" "Groovy" "Java"]
       (distinct ["Clojure" "Groovy" "Java" "Java" "Java" "Clojure"])))

;; String is also a collection we can invoke distinct function on.
(is (= [\a \b \c \d \e \f] (distinct "aabccdeff")))

;; For example a collection of mouse clicks where
;; we want to get rid of duplicate clicks at the same position.
(is (= [{:x 1 :y 1} {:x 1 :y 2} {:x 0 :y 0}]
       (distinct '({:x 1 :y 1} {:x 1 :y 2} {:x 1 :y 1} {:x 0 :y 0}))))

;; When we don't need the sequence result with ordening we can
;; also use a set to remove duplicates.
;; We loose the order of the elements.
(is (= #{1 5 6 2 3}
       (set [1 5 6 5 2 3 1])
       (into #{} [1 5 6 5 2 3 1])))



;; In the following example we have the results
;; from several throws with a dice and we want
;; remove duplicates that are thrown after another.
(is (= [1 5 6 2 3 1] (dedupe [1 5 5 6 2 3 3 1])))

;; Only consecutive duplicates are removed.
(is (= ["Clojure" "Groovy" "Java" "Clojure"]
       (dedupe ["Clojure" "Groovy" "Java" "Java" "Java" "Clojure"])))

;; String is also a collection.
(is (= [\a \b \c \d \e \f] (dedupe "aabccdeff")))

;; For example a collection of mouse clicks where
;; we want to get rid of consecutive clicks at the same position.
(is (= [{:x 1 :y 2} {:x 1 :y 1} {:x 0 :y 0}]
       (dedupe '({:x 1 :y 2} {:x 1 :y 1} {:x 1 :y 1} {:x 0 :y 0}))))