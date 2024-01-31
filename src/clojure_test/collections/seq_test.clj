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
  (is
   (= "mrhaki lives in Tilburg"
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

(is
 (= "Apple, Pear and 3 more fruit names."
    (summary basket)))

;; Notice that y is iterated over for every x. x could be seen as the 'outer' loop
(doseq [x [-1 0 1]
        y [1 2 3]]
  (prn (* x y)))

(doseq [[x y] (map list [1 2 3] [1 2])]
  (prn (* x y)))

(map list [1 2 3] [1 2 3])


(doseq [[[_ b] [_ d]] (map list (sorted-map :1 1 :2 2) (sorted-map :3 3 :4 4))]
  (prn (* b d)))


(doseq [[k v] (map identity {:1 1 :2 2 :3 3})]
  (prn k v))

;;等价于

(doseq [[k v] {:1 1 :2 2 :3 3}]
  (prn k v))

(doseq [a [1 2]
        b [3 4]]
  (println a b))

(doseq [x     (range 6)
        :when (odd? x)
        :let  [y (* x x)]]
  (println [x y]))


(doseq [x      (range 99)
        :let   [y (* x x)]
        :while (< y 30)]
  (println [x y]))


(for [x     (range 5)
      :when (> (* x x) 3)]
  (* 2 x))

(def v (vec (range 1 65)))
(def m (map #(do (prn %1) (str %1 "!")) v))

(println (first m))

;;(range 1 65)没有赋值给变量,不能惰性求值
(def v (vec (range 1 65)))
(def m (map #(do (prn %1) (str %1 "!")) v))

(first m)

;; prints 1-32, followed by "1!"
(nth m 10)

;; prints "11!"

;;A large range is of type LongRange whereas an infinite range, which you can
;create with (range), is of type Iterate and is an iterator that yields values (similar to a
;generator in other programming languages, like Python).
;;赋值给变量,可以惰性求值
(def r (range 1 999999999))


;; prepend 1 to a list
(cons 1 '(2 3 4 5 6))

;; notice that the first item is not expanded
(cons [1 2] [4 5 6])

;; Extract values from a map, treating keywords as functions.
((juxt :a :b) {:a 1 :b 2 :c 3 :d 4})

((juxt identity name) :keyword)

;; Segregate even and odd numbers in collection.
((juxt (partial filter even?) (partial filter odd?)) (range 0 9))

(into {} (map (juxt identity name) [:a :b :c :d]))

;; Get the first character and length of string
((juxt first count) "Clojure Rocks")

;; split a sequence into two parts
((juxt take drop) 3 [1 2 3 4 5 6])

;;keywords serve as getter functions to produce an ordered vector
((juxt :lname :fname) {:fname "Bill" :lname "Gates"})


;; something similar to group-by
;;Applies filter to the list, keeping only the positive numbers: (4 5 3).
;;Applies remove to the list, removing the positive numbers: (-1 -2 -9).
(def split-by (juxt filter remove))

(split-by pos? [-1 -2 4 5 3 -9])

(defn add-n [n, coll]
  (lazy-seq
    (cons
     (+ n (first coll))
     (add-n n (rest coll)))))
