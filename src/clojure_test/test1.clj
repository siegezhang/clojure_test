(ns clojure_test.test1
  (:require [clojure.test :refer :all]))

(deftest test1
  (testing "FIXME, I fail."
    (is (= 0 1))))


(deftest test2
  (testing
    (is (= 0 1))))