(ns clojure_test.collections.collection_test
  (:require [clojure.set :refer [map-invert]]
            [clojure.test :refer [is]]
            )
  (:import (java.util Locale)))

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

;; Vector of some JVM languages.
(def languages ["Java" "Kotlin" "Clojure" "Groovy"])

;; Using only the start index argumnt we get all items
;; from the start index to the end.
(is (= ["Clojure" "Groovy"] (subvec languages 2)))

;; When we use the start and end index arguments
;; we get the items from start to the given end.
(is (= ["Clojure"] (subvec languages 2 3)))


;; The split-with function has a predicate and returns the result
;; of the functions take-while and drop-while in a result vector.
(let [less-than-5? (partial > 5)
      numbers (range 11)]
  (is (= ['(0 1 2 3 4) '(5 6 7 8 9 10)]
         (split-with less-than-5? numbers))
      [(take-while less-than-5? numbers) (drop-while less-than-5? numbers)]))

;; In this example we take while the value is a String value and
;; drop while starting from first value that is not a String.
(letfn [(string-value? [[k v]] (instance? String v))]
  (is (= ['([:language "Clojure"] [:alias "mrhaki"]) '([:age 47] [:country "NL"])]
         (split-with string-value? {:language "Clojure" :alias "mrhaki" :age 47 :country "NL"}))))


;; Instead of splitting on a predicate we can just give the number
;; of elements we want to split on with the split-at function.
(is (= ['(0 1 2 3) '(4 5 6 7)]
       (split-at 4 (range 8))
       [(take 4 (range 8)) (drop 4 (range 8))]))

(is (= ['([:language "Clojure"] [:alias "mrhaki"] [:age 47]) '([:country "NL"])]
       (split-at 3 {:language "Clojure" :alias "mrhaki" :age 47 :country "NL"})))

;; shuffle will return a new collection
;; where the items are in a different order.
(shuffle (range 5))                                         ;; Possible collection [4 0 1 2 3]
(shuffle (range 5))                                         ;; Possible collection [1 3 4 2 0]

;; Define a deck of cards.
(def cards (for [suite [\♥ \♠ \♣ \♦]
                 symbol (concat (range 2 11) [\J \Q \K \A])]
             (str suite symbol)))

;; Some checks on our deck of cards.
(is (= 52 (count cards)))
(is (= (list "♥2" "♥3" "♥4" "♥5" "♥6" "♥7" "♥8" "♥9" "♥10" "♥J" "♥Q" "♥K" "♥A")
       (take 13 cards)))

;; Let's shuffle the deck. We get a new collection of cards ordered randomly.
(def shuffled-deck (shuffle cards))

;; Shuffled deck contains all items from the cards collection.
(is (true? (every? (set cards) shuffled-deck)))

;; We can take a number of cards.
(take 5 shuffled-deck)                                      ;; Possible result: ("♦6" "♦10" "♥K" "♥4" "♥10")

;; We do a re-shuffle and get different cards now.
(take 5 (shuffle shuffled-deck))                            ;; Possible result: ("♥10" "♥Q" "♦4" "♣8" "♠5")

;; Create new string with format string as template as first argument.
;; Following arguments are used to replace placeholders in the
;; format string.
;; Clojure will delegate to the java.lang.String#format method and
;; we can use all format string options that are defined for this method.
;; More details about the format string syntax can be found in
;; java.util.Formatter. In a REPL we can find the docs
;; with (javadoc java.util.Formatter).
(is (= "https://www.mrhaki.com/"
       (format "https://%s/" "www.mrhaki.com")))

;; Format string with argument index to refer to one argument twice.
(is (= "clojure CLOJURE"
       (format "%1$s %1$S" "clojure")))

;; Format string to define fixed result lenght of 10 characers
;; with padding to get the given length.
(is (= "   Clojure"
       (format "%10s" "Clojure")))

;; Default Locale is used to determine how locale specific
;; formats are applied. In the following example the default
;; decimal separator is . and group separator is , as specified
;; for the Canadian Locale.
(Locale/setDefault Locale/CANADA)
(is (= "Total: 42,000.00"
       (format "Total: %,.2f", 42000.0)))

(defn format-locale
  "Format a string using String/format with a Locale parameter"
  [locale fmt & args]
  (String/format locale fmt (to-array args)))

;; We can use a different Locale to apply different specific
;; locale formats. In the next example we use the Dutch Locale
;; and the decimal seperator is , and the group separator is ..
(is (= "Totaal: 42.000,00"
       (format-locale (Locale. "nl") "Totaal: %,.2f" 42000.0)))

;; Vector of some JVM languages.
(def languages ["Java" "Kotlin" "Clojure" "Groovy"])

;; Using only the start index argumnt we get all items
;; from the start index to the end.
(is (= ["Clojure" "Groovy"] (subvec languages 2)))

;; When we use the start and end index arguments
;; we get the items from start to the given end.
(is (= ["Clojure"] (subvec languages 2 3)))


;; The split-with function has a predicate and returns the result
;; of the functions take-while and drop-while in a result vector.
(let [less-than-5? (partial > 5)
      numbers (range 11)]
  (is (= ['(0 1 2 3 4) '(5 6 7 8 9 10)]
         (split-with less-than-5? numbers))
      [(take-while less-than-5? numbers) (drop-while less-than-5? numbers)]))

;; In this example we take while the value is a String value and
;; drop while starting from first value that is not a String.
(letfn [(string-value? [[k v]] (instance? String v))]
  (is (= ['([:language "Clojure"] [:alias "mrhaki"]) '([:age 47] [:country "NL"])]
         (split-with string-value? {:language "Clojure" :alias "mrhaki" :age 47 :country "NL"}))))


;; Instead of splitting on a predicate we can just give the number
;; of elements we want to split on with the split-at function.
(is (= ['(0 1 2 3) '(4 5 6 7)]
       (split-at 4 (range 8))
       [(take 4 (range 8)) (drop 4 (range 8))]))

(is (= ['([:language "Clojure"] [:alias "mrhaki"] [:age 47]) '([:country "NL"])]
       (split-at 3 {:language "Clojure" :alias "mrhaki" :age 47 :country "NL"})))