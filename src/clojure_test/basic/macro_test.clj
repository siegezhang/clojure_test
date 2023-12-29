(ns clojure_test.basic.macro_test
  )

(defmacro infix "Use this macro when you pine for the notation of your childhood" [infixed] (list (second infixed) (first infixed) (last infixed)))
(println (infix (1 + 1)))