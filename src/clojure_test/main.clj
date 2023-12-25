(ns clojure_test.main
  (:gen-class))
(defmacro infix "Use this macro when you pine for the notation of your childhood" [infixed] (list (second infixed) (first infixed) (last infixed)))


(defn -main
  [& args]
  (println (= (list :a :b :c
                    ) '(:a :b :c))
           )
  (def user-data {:name "John Doe" :age 30})
  (println user-data)

  (println (= [:a :b :c] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c)))
  (println (= [:a :b :c] (list :a :b :c)))

  (= 8 ((fn add-five [x] (+ x 5)) 3))
  (= 8 ((fn [x] (+ x 5)) 3))
  (= 8 (#(+ % 5) 3))
  (= 8 ((partial + 5) 3))

  (println (= '(6 7 8) (map #(+ % 5) '(1 2 3))))
  (println (= '(6 7) (filter #(> % 5) '(3 4 5 6 7))))

  (println (= ((fn a [l] (nth l (- (count l) 1))) [1 2 3 4 5]) 5))
  (= ((fn a [l] (nth l (+ (count l) -2))) (list 1 2 3 4 5)) 4)

  ;(= (__ [1 2 3 4 5]) 5)
  ;(= (__ '(5 4 3)) 3)
  ;(= (__ ["b" "c" "d"]) "d")
  (let [[head & tail] '(1 2 3)] (println head) (println tail))

  (println ((defn get-nth [seq n] (first (drop n seq))) '(4 5 6 7) 2))
  (println (= (#(reduce + (map (fn [x] 1) %)) '(1 2 3 3 1)) 5))

  reduce #(cons %2 %1) '()



  (println (infix (1 + 1)))

  )



