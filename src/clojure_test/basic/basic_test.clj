(ns clojure_test.basic.basic_test
  (:require [clojure.test :refer [is]]))

;; Only nil and false are falsey.
(is (= false (boolean false)))
(is (= false (boolean nil)))
(is (= false (boolean ({:cool "Clojure"} :language))))

(is (= :falsey (if nil :truthy :falsey)))

;; Other values are truthy.
(is (= true (boolean true)))
(is (= true (boolean "mrhaki")))
(is (= true (boolean 0)))
(is (= true (boolean 1)))
(is (= true (boolean '())))
(is (= true (boolean [1 2 3])))
(is (= true (boolean {})))
(is (= true (boolean ({:rocks "Clojure"} :rocks))))

(is (= :truthy (if "Clojure" :truthy :falsey)))
(is (= :truthy (if "" :truthy :falsey)))
(is (= :empty-but-truthy (if [] :empty-but-truthy :empty-and-falsey)))


(println
 (clojure.string/replace "This is _simple_ markup."
                         #"_([^_]+)_"
                         (fn [[_ s]]
                           (str "<strong>"
                                (clojure.string/upper-case s)
                                "</strong>"))))

(println (remove pos? [1 -2 2 -1 3 7 0]))

(println (remove nil? [1 nil 2 nil 3 nil]))

(remove #(zero? (mod % 3)) (range 1 21))

(remove even? (range 10))

(remove
 (fn [x]
   (= (count x) 1))
 ["a" "aa" "b" "n" "f" "lisp" "clojure" "q" ""])


(defn re-map [re f s]
  (remove #{::padding}
          (interleave
           (clojure.string/split s re)
           (concat (map f (re-seq re s)) [::padding]))))


(defn re-map1 [re f s]
  (interleave
   (clojure.string/split s re)
   (concat (map f (re-seq re s)) [::padding])))


(println
 (re-map #"_([^_]+)_"
         (fn [[_ s]]
           [:strong s])
         "This is _simple_ markup."))

(defn re-map [re f s]
  (if (re-matches re s)
    [(f s)]
    (remove #{"" ::padding}
            (interleave
             (clojure.string/split s re)
             (concat (map f (re-seq re s)) [::padding])))))

(re-map #"hello" (constantly :match) "hello")

(let [id 1]
  (let [id 2]
    (println id))
  (println id))

;定义一个两个数字相加的匿名函数，并绑定到 add 上
(def add (fn [a, b] (+ a b)))

;等价的形式
(def add #(+ %1 %2))

;等价的形式
(defn add [a, b] (+ a b))


;clojure 是 lisp 的一种方言。lisp 是 “List  Processor” 的缩写，就是列表解析的意思，
;使用列表来表示所有的东西（S 表达式）。从我们写的代码也可以看出，整个代码结构就是一个嵌套的列表
(defn select-random
  "从一个列表中随机返回一个元素"
  {:added "1.2"} ;; 元数据
  [options]
  (nth options (rand-int (count options))))


;虽然只定义了一个元数据:add，但是系统却给我们返回了一堆元数据。这些元数据是系统默认给函数添加了，主要是函数的一些基本信息。下面是一些比较重要的信息:

;:ns 命名空间
;:name 函数名
;:file 对应的源码文件
;:arglists 参数列表 （一个函数刻意包含多个参数列表（见上篇），所以是 lists 而不是 list）
;:doc 函数描述
(println (meta #' select-random))
(println (meta (var select-random)))
(println (var select-random))

;我们可不仅限于只给函数添加元数据。任何能绑定变量的都可以添加元数据，例如符号或者其他数据结构。
(def approaches
  (with-meta
    (list "ferocious" "wimpy" "precarious")
    {:creator "tim"}))

(println (meta approaches))

(defn greeting
  "Composes a greeting sentence. Expects both the name of a greeter
   and the name of whom is to be greeted for arguments. An approach
   and an action are randomly selected."
  {:added "1.2"}
  [greeter whom]
  ;;str 用于组装字符串
  (str greeter " greeted " whom " with a "
       (select-random (list "ferocious" "wimpy" "precarious" "subtle")) " "
       (select-random (list "growl" "lick" "jump")) "!"))

(println (greeting "siege" "me"))

;三种不同参数形式的 add 函数。
;这样调用的话 (add 1 2) 会匹配第一种形式，正确返回 3。
;(add 1 2 3) 会匹配第二种参数模式，返回结果 6。这其实就是 lisp 中模式匹配的一种应用
(defn add
  ([v1 v2] (+ v1 v2))
  ([v1 v2 v3] (+ v1 v2 v3))
  ([v1 v2 v3 v4] (+ v1 v2 v3 v4)))

;add 支持任意个数字参数相加
(defn add [v1 v2 & others]
  ;;&后面的是可变参数
  (+ v1 v2
     (if others                                             ;;判断可变参数列表是否是空，如果不是累加列表中的值，否则返回0
       (reduce + 0 others)
       ;;使用reduce函数计算others的数字之和。
       0)))

;如果你不想让你列表中的元素被解释执行，记得引用（quote）一下。
(println (list "truck" "car" "bicycle" "plane"))

;;创建一个列表
(println '("truck" "car" "bicycle" "plane"))

;;查看列表的类型

(class '("truck" "car" "bicycle" "plane"))


;;给创建的列表绑定一个全局变量
(def vehicles '("truck" "car" "bicycle" "plane"))
