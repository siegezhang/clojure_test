(ns clojure_test.database.routes
  (:require [clojure.data.json :as json]
            [clojure_test.database.query :as query]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route :as route]))
(def http-result
  {:success {:status 200}})

(def db-interceptor
  {:name :db-interceptor
   :enter (fn [context] (assoc context ::guid (java.util.UUID/randomUUID)))})

(defn hello-function
  [request]
  (let [name (get-in request [:query-params :name] "Everybody")]
    {:status 200 :body (str "Hello World! " name)}))

(defn create-item
  [request]
  (let [body (:json-params request)
        title (get-in body [:title])
        description (get-in body [:description])
        queryResult (query/add-item title description)
        idInserted (get-in queryResult [:generated_key])
        result (http-result :success)
        body (json/write-str (assoc body :id idInserted))]
    (-> result
        (assoc :body body))))

  (defn get-items
    [_]
    (let [queryResult (query/get-items)
          result (http-result :success)
          body (json/write-str queryResult)]
      (-> result
          (assoc :body body))))

(defn delete-item
  [request]
  (let [item-id (get-in request [:path-params :id])]
    (query/delete-item (Integer/parseInt item-id))
    (http-result :success)))

(defn update-item
  [request]
  (let [item-id (get-in request [:path-params :id])
        body (:json-params request)
        title (body :title)
        description (body :description)
        result (http-result :success)]
    (query/update-item item-id title description)
    (-> result
        (assoc :body body))))

(def routes (route/expand-routes
             #{["/hello" :get hello-function :route-name :hello-world]
               ["/item" :get [db-interceptor get-items] :route-name :get-item]
               ["/item"  :post [db-interceptor (body-params/body-params) create-item] :route-name :create-item]
               ["/item/:id" :put [db-interceptor (body-params/body-params) update-item] :route-name :update-item]
               ["/item/:id" :delete [db-interceptor delete-item] :route-name :delete-item]}))