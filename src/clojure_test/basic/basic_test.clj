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


(println (clojure.string/replace "This is _simple_ markup."
                                 #"_([^_]+)_"
                                 (fn [[_ s]]
                                   (str "<strong>"
                                        (clojure.string/upper-case s)
                                        "</strong>"))))

(remove pos? [1 -2 2 -1 3 7 0])

(remove nil? [1 nil 2 nil 3 nil])

(remove #(zero? (mod % 3)) (range 1 21))

(remove even? (range 10))

(remove (fn [x]
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


(println (re-map #"_([^_]+)_"
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
