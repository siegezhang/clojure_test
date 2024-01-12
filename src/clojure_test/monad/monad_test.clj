(ns clojure_test.monad.monad_test)


(use
 'clojure.algo.monads)

((fn [a]
   ((fn [b] (* a b))
     (inc a)))
  1)

;;等价于
(let [a 1
      b (inc a)]
  (* a b))

;;等价于
(m-bind 1
        (fn [a]
          (m-bind (inc a)
                  (fn [b]
                    (* a b)))))


(domonad identity-m
         [a 1 b (inc a)] (* a b))