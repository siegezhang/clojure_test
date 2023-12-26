(ns clojure_test.file.file_test
  (:require [clojure.java.io :as io :refer [make-parents file]]
            [clojure.test :refer [is]])
  (:import (java.io File)))

;; slurp interperts a String value as a file name.
(is (= "Clojure rocks!" (slurp "files/README")))
;; Using the encoding option.
(is (= "Clojure rocks!" (slurp "files/README" :encoding "UTF-8")))

;; We can also use an explicit File object.
(is (= "Clojure rocks!" (slurp (io/file "files/README"))))
(is (= "Clojure rocks!" (slurp (File. "files/README"))))

;; We can also use an URL as argument.
;; For example to read from the classpath:
(is (= "Clojure rocks!" (slurp (io/resource "data/README"))))

;; Or HTTP endpoint
(is (= "Clojure rocks!" (slurp "https://www.mrhaki.com/clojure.txt")))


;; make-parents will create the parents directories for a file
;; The function returns true if the directories are created,
;; false if the directories already exist.
(let [file (file "tmp" "clojure" "sample.txt")]
  (is (true? (make-parents file)) "All parent directories are created")
  (is (false? (make-parents file)) "Second time the directory already exists"))

;; make-parents uses the same arguments as the clojure.java.io.file function
(is (true? (make-parents "tmp" "clj" "sample.txt")) "Directories tmp/clj are created")
