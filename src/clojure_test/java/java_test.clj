(ns clojure_test.java.java_test
  (:require [clojure.test :refer [is]])
  (:import (java.net URI)
           (java.util Optional)
           (java.text MessageFormat)
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

(def value "Clojure")

;; We use Optional map method that accepts a java.util.function.Function,
;; so here we implement the Function interface with an implementation
;; to return the given String value in upper case.
(def fn-upper
  (reify java.util.function.Function
    (apply [this arg] (. arg toUpperCase))))

(is (= "CLOJURE"
       ;; Invoke Java method chaining using the special .. macro.
       ;; Java: Optional.ofNullable(value).map(s -> s.toUpperCase()).orElse("Default")
       (.. Optional (ofNullable value) (map fn-upper) (orElse "Default"))

       ;; Macro expands to the following equivalent using . form.
       (. (. (. Optional ofNullable value) (map fn-upper)) (orElse "Default"))

       ;; Using thread first macro with equivalent method invocations.
       (-> (Optional/ofNullable value)
           (. (map fn-upper))
           (. (orElse "Default")))))

(is (= "Default"
       (.. Optional (ofNullable nil) (map fn-upper) (orElse "Default"))))


;; We want to invoke the Java method MessageFormat/format that accepts
;; a format parameter followed by a varargs parameter.
(is (thrown-with-msg? ClassCastException
                      #"java.lang.String incompatible with \[Ljava.lang.Object;"
                      (MessageFormat/format "{0} is awesome." "Clojure")))

;; Use into-array to transform sequence to Java array,
;; that can be used for methods that accept varargs parameter.
(is (= "Clojure is awesome."
       (MessageFormat/format "{0} is awesome."
                             (into-array ["Clojure"]))))

;; In the next example the type of the array is based on the first element
;; and becomes String[], but the sequence has also elements with other types.
;; We use the argment Object to have an Object[] array.
(is (= "Clojure contains 7 characters."
       (MessageFormat/format "{0} contains {1} characters."
                             (into-array Object ["Clojure" (count "Clojure")]))))

;; To get an Object[] array we could also use to-array function
;; with a collection argument.
(is (= "Clojure contains 7 characters."
       (MessageFormat/format "{0} contains {1} characters."
                             (to-array ["Clojure" (count "Clojure")]))))

;; Type of first element sets array type.
(is (= "[Ljava.lang.String;"
       (.getName (class (into-array ["Clojure" "Groovy"])))))

;; Use explicit type or to-array function that always returns Object[] array.
(is (= "[Ljava.lang.Object;"
       (.getName (class (into-array Object ["Clojure" "Groovy"])))
       (.getName (class (to-array ["Clojure" "Groovy"])))))

;; Primitive types are transformed to array of boxed type: short, becomes Short.
(is (= "[Ljava.lang.Short;"
       (.getName (class (into-array (map short (range 5)))))))

;; We can get primitive type by using TYPE field of boxed type or
;; specific <primitive>-array function, like short-array.
(is (= "[S"
       (.getName (class (into-array Short/TYPE (map short (range 5)))))
       (.getName (class (short-array (range 5))))))