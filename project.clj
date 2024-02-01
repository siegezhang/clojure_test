(defproject clojure_test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0-alpha5"]
                 [org.clojure/clojure-contrib "1.2.0"] [io.pedestal/pedestal.service "0.6.3"]
                 [io.pedestal/pedestal.route "0.6.3"]
                 [io.pedestal/pedestal.jetty "0.6.3"]
                 [org.clojure/data.json "0.2.6"]
                 [org.slf4j/slf4j-simple "1.7.28"]
                 [korma "0.5.0-RC1"] [org.clojure/core.match "1.0.1"]
                 [clj-time/clj-time "0.15.2"] [com.mysql/mysql-connector-j "8.2.0"]
                 [org.clojure/algo.monads "0.1.6"]]

  ;[mysql/mysql-connector-java "8.0.13"]




  :repl-options {:init-ns clojure-test.core})
