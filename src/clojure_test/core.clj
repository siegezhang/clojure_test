(ns clojure-test.core
  (:require
    [clojure.test :refer :all]))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(deftest a-test
  (testing
    (is (= 1 1))))

