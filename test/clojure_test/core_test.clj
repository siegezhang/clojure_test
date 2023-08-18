(ns clojure-test.core-test
  (:require [clojure-test.core :refer :all]
            [clojure.test :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(deftest a-test
  (testing
    (is (= 1 1))))