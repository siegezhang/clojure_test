(ns clojure_test.basic.macro_test1)

(defmacro cd
  "change current directory"
  [path & cmd]
  `(str "cd " ~path "; " ~@cmd))

(defmacro run
  "simply run a command"
  [cmd]
  `(str ~cmd "; "))


(println (cd " /home/siege"
             (run " mkdir he")))

(println (macroexpand '(cd "/home/siege"
                           (run " mkdir he"))))

;;了解'和`的区别,`叫做Syntax Quote
(println `(defn hello-world [] (println "Hello, data! Or code??")))




