(ns clojure_test.basic.macro_test3)


(println `(1 2 ~(list 3 4)))

(println `(1 2 ~@(list 3 4)))

(println `(1 ~@(list 2 3) 4 ~@(list 5 6) 7))
(println `[1 ~@(list 2 3) 4 ~@(list 5 6) 7])
(println `#{1 ~@(list 2 3) 4 ~@(list 5 6) 7})
(println `(1 ~@'(2 3) 4 ~@[5 6] 7 ~@(range 8 10)))
(println `(1 ~@{2 3 4 5} 6))
(println `{1 ~@[2 3] 4 ~@[5 6]})
(println `(hash-map 1 2 ~@[3 4]))


(def names
  '("Burke", "Frank", "Connor", "Albert", "Everett" "George", "Harris", "David"))


(defmacro from [var _ coll _ condition _ ordering _ desired-map]
  `(map (fn [~var] ~desired-map)
    (sort-by (fn [~var] ~ordering) (filter (fn [~var] ~condition) ~coll))))

(def query
  (from n in names
        where (= (. n length) 5)
        orderby n
        select (. n toUpperCase)))

(doseq [n query] (println n))



