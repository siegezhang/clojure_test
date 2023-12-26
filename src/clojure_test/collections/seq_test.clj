(ns clojure_test.collections.seq_test
  (:require [clojure.test :refer [is]]))

(def items ["mrhaki" "Hubert Klein Ikkink" "Tilburg"])

;; Elements from the items vector are positionally
;; destructured to symbols.
(let [[alias name city] items]
  (is (= "mrhaki" alias))
  (is (= "Hubert Klein Ikkink" name))
  (is (= "Tilburg" city)))

;; When we define a symbol but there are no elements
;; to assign a value, the symbol will be nil.
(let [[alias name city country] items]
  (is (nil? country)))

;; When we don't need the destructured symbol we can
;; use the underscore to indicate this. But any name will do.
(let [[username _ city] items]
  (is (= "mrhaki lives in Tilburg"
         (str username " lives in " city))))

;; We can destructure sequences just like vectors.
(def coords '(29.20090, 12.90391))

(let [[x y] coords]
  (is (= 29.20090 x))
  (is (= 12.90391 y)))

(let [[first-letter _ third-letter] "mrhaki"]
  (is (= \m first-letter))
  (is (= \h third-letter)))


;; We can nest our destructure definitions.
(def currencies [[42 "EUR"] [50 "USD"]])

;; We want the second value of the first element and
;; the first value of the second element.
(let [[[_ currency] [amount _]] currencies]
  (is (= "EUR" currency))
  (is (= 50 amount)))

;; Example sequence with fruit names.
(def basket '("Apple" "Pear" "Banana" "Grapes" "Lemon"))

;; We can use & to assign all remaining not-yet
;; destructured element to a sequence.
(let [[first second & rest] basket]
  (is (= "Apple" first))
  (is (= "Pear" second))
  (is (= ["Banana" "Grapes" "Lemon"] rest)))

;; We can use :as to get the original sequence.
(let [[first _ third :as fruits] basket]
  (is (= "Apple" first))
  (is (= "Banana" third))
  (is (= "APBGL" (apply str (map #(.charAt % 0) fruits)))))


;; Use destructure in function parameter to
;; destructure the argument value when invoked.
(defn summary
  [[first second :as all]]
  (str first ", " second " and " (- (count all) 2) " more fruit names."))

(is (= "Apple, Pear and 3 more fruit names."
       (summary basket)))