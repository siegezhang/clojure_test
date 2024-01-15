(ns clojure_test.basic.macro_test2)


;; https://www.zhihu.com/question/401288038/answer/1282550454
;;利用macro实现一个和python一样的写法,该例子是lisp,clojure可能需要修改
;;x = [i for i in range(10) if i % 2 == 0]

(defmacro lcomp (expression for var in list conditional conditional-test)
  ;; create a unique variable name for the result
  (let ((result (gensym)))
    ;; the arguments are really code so we can substitute them
    ;; store nil in the unique variable name generated above
    `(let ((, result nil))
       ;; var is a variable name
       ;; list is the list literal we are suppose to iterate over
       (loop for, var in, list
                  ;; conditional is if or unless
                  ;; conditioanl-test is (= (mod x 2) 0) in our examples, conditional, conditional-test
                  ;; and this is the action from the earlier lisp example
                  ;; result = result + [x] in python
                  do (setq, result (append, result (list, expression)))))))
;; return the result, result)))

(lcomp x for x in (range 10) if (= (mod x 2) 0))





