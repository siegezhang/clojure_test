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