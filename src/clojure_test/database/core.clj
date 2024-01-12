(ns clojure_test.database.core
  (:gen-class) 
  (:require [clojure_test.database.server :as server]))

(defn -main [& _]
  (server/start))