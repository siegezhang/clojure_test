(ns clojure_test.basic.macro_test4)


;;了解下面的不同

(defmacro parameterized-multi-minimal [n-times s]
  (cons 'do (repeat n-times '(println s))))

(defmacro parameterized-multi-minimal1 [n-times s]
  (concat (list 'let ['string-to-print s])
          (repeat n-times '(println string-to-print))))