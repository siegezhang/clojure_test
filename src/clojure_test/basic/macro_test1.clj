(ns clojure_test.basic.macro_test1)

(defmacro cd
  "change current directory"
  [path & cmd]
  `(str "cd " ~path "; " ~@cmd))

(defmacro run
  "simply run a command"
  [cmd]
  `(str ~cmd "; "))


(println
 (cd " /home/siege"
     (run " mkdir he")))

(println
 (macroexpand
  '(cd "/home/siege"
    (run " mkdir he"))))

;;了解'和`的区别,'叫simple quoting,`叫做Syntax Quote
;;syntax quoting跟simple quoting不同的地方在于，我们可以在syntax quoting里面使用~来unquote一些form，
;; 这等于是说，我要quote这一个expression，但是这个expression里面某一个form先evaluate
;;Unlike defn, defmacro does not provide a context for runtime variable bindings.
(println `(defn hello-world [] (println "Hello, data! Or code??")))

;; Define a macro using defmacro. Your macro should output a list that can
;; be evaluated as clojure code.
;;
;; This macro is the same as if you wrote (reverse "Hello World")
(defmacro my-first-macro []
  (list reverse "Hello World"))

;; Inspect the result of a macro using macroexpand or macroexpand-1.
;;
;; Note that the call must be quoted.
(macroexpand '(my-first-macro))

;; -> (#<core$reverse clojure.core$reverse@xxxxxxxx> "Hello World")

;; You can eval the result of macroexpand directly:
(eval (macroexpand '(my-first-macro)))

; -> (\d \l \o \r \W \space \o \l \l \e \H)

;; But you should use this more succinct, function-like syntax:
(my-first-macro)

; -> (\d \l \o \r \W \space \o \l \l \e \H)

;; You can make things easier on yourself by using the more succinct quote syntax
;; to create lists in your macros:
(defmacro my-first-quoted-macro []
  '(reverse "Hello World"))

(macroexpand '(my-first-quoted-macro))

;; -> (reverse "Hello World")
;; Notice that reverse is no longer function object, but a symbol.

;; Macros can take arguments.
(defmacro inc2 [arg]
  (list + 2 arg))

(inc2 2)

; -> 4

;; But, if you try to do this with a quoted list, you'll get an error, because
;; the argument will be quoted too. To get around this, clojure provides a
;; way of quoting macros: `. Inside `, you can use ~ to get at the outer scope
(defmacro inc2-quoted [arg]
  `(+ 2 ~arg))

(inc2-quoted 2)

;; You can use the usual destructuring args. Expand list variables using ~@
(defmacro unless [arg & body]
  `(if (not ~arg)
    (do ~@body)))

; Remember the do!

(macroexpand '(unless true (reverse "Hello World")))

;; ->
;; (if (clojure.core/not true) (do (reverse "Hello World")))

;; (unless) evaluates and returns its body if the first argument is false.
;; Otherwise, it returns nil

(unless true "Hello")

; -> nil
(unless false "Hello")

; -> "Hello"

;; Used without care, macros can do great evil by clobbering your vars
(defmacro define-x []
  '(do
    (def x 2)
    (list x)))

(def x 4)

(define-x)

; -> (2)
(list x)

; -> (2)

;; To avoid this, use gensym to get a unique identifier
(gensym 'x)

; -> x1281 (or some such thing)

(defmacro define-x-safely []
  (let [sym (gensym 'x)]
    `(do
      (def ~sym 2)
      (list ~sym))))

(def x 4)

(define-x-safely)

; -> (2)
(list x)

; -> (4)

;; You can use # within ` to produce a gensym for each symbol automatically
(defmacro define-x-hygienically []
  `(do
    (def x# 2)
    (list x#)))

(def x 4)

(define-x-hygienically)

; -> (2)
(list x)

; -> (4)

;; It's typical to use helper functions with macros. Let's create a few to
;; help us support a (dumb) inline arithmetic syntax
(declare inline-2-helper)

(defn clean-arg [arg]
  (if (seq? arg)
    (inline-2-helper arg)
    arg))

(defn apply-arg
  "Given args [x (+ y)], return (+ x y)"
  [val [op arg]]
  (list op val (clean-arg arg)))

(defn inline-2-helper
  [[arg1 & ops-and-args]]
  (let [ops (partition 2 ops-and-args)]
    (reduce apply-arg (clean-arg arg1) ops)))

;; We can test it immediately, without creating a macro
(inline-2-helper '(a + (b - 2) - (c * 5)))

; -> (- (+ a (- b 2)) (* c 5))

; However, we'll need to make it a macro if we want it to be run at compile time
(defmacro inline-2 [form]
  (inline-2-helper form))

(macroexpand '(inline-2 (1 + (3 / 2) - (1 / 2) + 1)))

; -> (+ (- (+ 1 (/ 3 2)) (/ 1 2)) 1)

(inline-2 (1 + (3 / 2) - (1 / 2) + 1))

; -> 3 (actually, 3N, since the number got cast to a rational fraction with /)

(println (gensym))
(println (gensym))
(println (gensym "xyz"))
(println (gensym "xyz"))

(defmacro make-adder [x]
  (let [y (gensym)]
    `(fn [~y] (+ ~x ~y))))

(defmacro make-adder1 [x]
  `(fn [y#] (+ ~x y#)))

(defmacro info-about-caller []
  (print {:form &form :env &env})
  `(println "macro was called!"))

(info-about-caller)

(let [foo "bar"] (info-about-caller))

(let [foo "bar"
      baz "quux"]
  (info-about-caller))

(defmacro inspect-caller-locals []
  (->> (keys &env)
       (map (fn [k] [`'~k k]))
       (into {})))

(inspect-caller-locals)

(let [foo "bar"
      baz "quux"]
  (inspect-caller-locals))

(defmacro inspect-caller-locals-1 []
  (->> (keys &env)
       (map (fn [k] [`(quote ~k) k]))
       (into {})))
(defmacro inspect-caller-locals-2 []
  (->> (keys &env)
       (map (fn [k] [(list 'quote k) k]))
       (into {})))

(inspect-caller-locals-1)

(inspect-caller-locals-2)

(let [foo "bar"
      baz "quux"]
  (inspect-caller-locals-1))

(let [foo "bar"
      baz "quux"]
  (inspect-caller-locals-2))

(defmacro inspect-called-form [& arguments]
  {:form (list 'quote (cons 'inspect-called-form arguments))})


(println (inspect-called-form 1 2 3))

(defmacro inspect-called-form [& arguments]
  {:form (list 'quote &form)})

^{:doc "this is good stuff"} (inspect-called-form 1 2 3)

(meta (:form *1))


(defmacro my-macro [x y]
  `(println (+ ~x y)))

(println 'symbol)

; Prints #<Symbol symbol> (the symbol itself)
(println `symbol)

; Prints symbol (the keyword "symbol")
;(my-macro 'x 10)   ; Prints 11 (x is quoted, becomes symbol, + evaluates)
;(my-macro `(+ x 1) 10)   ; Prints 12 (entire expression quoted, then evaluated)

;;https://stackoverflow.com/questions/26485514/clojure-difference-between-quote-and-syntax-quote
(def x 1)

(println '`~x)
(println `'~x)

(let [x 2]
  `(1 x 3))

(let [x 2]
  `(1 ~x 3))

`(1 (dec 3) 3)

`(1 ~(dec 3) 3)

(let [x `(2 3)]
  `(1 ~x))

(let [x `(2 3)]
  `(1 ~@x))


;; ' is the shortcut for quote
(= 'a (quote a))

;; quoting keeps something from being evaluated
(quote (println "foo"))

(println *clojure-version*)


;; Proof that ' is just a syntactic sugar for quote:
(println (macroexpand ''(1 2 3)))