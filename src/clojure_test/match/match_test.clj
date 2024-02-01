(require
 '[clojure.core.match :refer [match]])


(doseq [n (range 1 21)]
  (println
   (match [(mod n 3) (mod n 5)]
          [0 0] "FizzBuzz"
          [0 _] "Fizz"
          [_ 0] "Buzz"
          :else n)))


(let [x 1
      y 2
      z 3]
  (match [x y z]
         [1 2 b] [:a0 b]
         [a 2 3] [:a1 a]))


(match ['foobar]
       ['foobar] :bar
       ['bazqux] :qux)


(let [a (+ 1 1)]
  (match [99]
         [a] :success
    :else :fail))

(match [[:a :b :c]]
       [[:a :b _]] :success
  :else :fail)


(match [{:a 1 :b 1}]
       [{:a _ :b 2}] :foo
  [{:a 1 :b _}] :bar
  :else :baz)


(match
 [[:a 1
   :b 2
   :c 3
   :d 4]]
 [[:a 1
   :b 2]]
 "this would need to be complete match"
 [[:a 1
   :b _
   &
   rest]]
 "rest allows for a partial match"
 :else :fail)


(match [{:a 1 :b 2 :c 3 :d 4}]
       [({:a _ :b 2} :only [:a :b :c])] "Didn't match, :only expects three keys"
       [({:a _ :b 2} :only [:a :b :c :d])] "Match!"
       [{:a _ :b 2}] "this assertion is never executed"
       :else :fail)


(match [[1 2 3]]
       [[1 (:or 3 4) 3]] :foo
       [[1 (:or 2 3) 3]] :bar)


(match [{:a 3}]
       [{:a (:or 1 2)}] :foo
       [{:a (:or 3 4)}] :bar)


(defn div3? [n]
  "A function that returns true or false
  if the parameter can be evenly divided by three"
  (if (= (mod n 3) 0) true false))

(match [[2 3 4 5]]
       [[_ (a :guard even?) _ _]] (format "We matched first %d" a)
       [[_ (b :guard [odd? div3?]) _ _]] (format "We matched second %d" b))