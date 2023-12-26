(ns clojure_test.java.java_test
  (:require [clojure.test :refer [is]])
  (:import (java.net URI)
           (java.util Map TreeMap)))

;; Using a dot after the class name to invoke the constructor.
(is (instance? String (String.)))

;; Or using the new special form to invoke the constructor.
(is (instance? String (new String)))

;; Constructor arguments can be used.
(is (instance? URI (URI. "https://www.mrhaki.com")))
(is (instance? URI (new URI "https" "www.mrhaki.com" "/" "")))

;; We can use Clojure data structures in constructors.
(is (instance? Map (TreeMap. {:language "Clojure"})))