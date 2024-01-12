(ns clojure_test.basic.macro_test)


(defmacro infix "Use this macro when you pine for the notation of your childhood" [infixed] (list (second infixed) (first infixed) (last infixed)))
(println (infix (1 + 1)))


(defmacro cond-let [nm & clauses]
  (when (seq clauses)
    `(if-let [~nm ~(first clauses)]
       ~(second clauses)
       (cond-let ~nm ~@(drop 2 clauses)))))

(println (let [word "beta"]
           (cond-let result
                     (some #{\x \y \z} word), (str "xyz: " result)
                     (some #{\a \b \c} word), (str "abc: " result))))


(defmacro parallel-hash-map [m]
  (let [ks (vec (keys m))
        vs (vals m)]
    `(zipmap ~ks (pvalues ~@vs))))

(println (time
           (parallel-hash-map
             {:a (do (Thread/sleep 100) 1)
              :b (do (Thread/sleep 100) 2)})))


(defmacro kv-map [& symbols]
  `(zipmap
     ~(mapv keyword symbols)
     ~(vec symbols)))
(let [a 1
      b 2]
  (kv-map a b))