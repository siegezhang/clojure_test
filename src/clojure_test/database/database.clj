(ns clojure_test.database.database
  (:require [korma.db :as korma]))

(def db-connection-info
  (korma/mysql
    {:subname  "//192.168.3.3:3306/qm_his"
     :classname "com.mysql.cj.jdbc.Driver"
     :user     "root"
     :password "WZ$l09wjzx9w2LfGF0B)1A"}))
; set up korma
(korma/defdb db db-connection-info)