(ns liberator-example.core
  (:require [liberator.core :refer (resource defresource)]
            [liberator.representation :refer (ring-response as-response)]
            [liberator-example.data :refer (data)]
            [ring.middleware.params :refer (wrap-params)]
            [hiccup.core :refer (html)]
            [ring.adapter.jetty :as jetty]
            [bidi.ring :refer (make-handler)]
            [cylon.user.totp :as totp]))

(defn current-time []
  (format "It's %d milliseconds since the beginning of the epoch" (System/currentTimeMillis)))

(defresource foo
  :available-media-types ["text/html" "application/json"]
  :allowed-methods [:get :post ]
  :handle-ok (fn [ctx]
               (clojure.pprint/pprint ctx)
               (condp = (get-in ctx [:representation :media-type])
                 "text/html" (html [:span {:style "color:white;background:black;"}
                                    (current-time)])
                 "application/json" {:message (current-time)})))

(defresource foo2
  :available-media-types ["text/html"]
  :handle-ok (fn [{{{x :id} :route-params} :request}]
               (format "foo2. x:%s" x))
  :allowed-methods [:get :head]
  )

(defn split [s r]
  (clojure.string/split s (re-pattern r)))



(defresource documents
  :available-media-types ["application/json"]
  :as-response (fn [d ctx]
                 (-> (as-response d ctx) ;; default implementation
                     (assoc-in [:headers "Access-Control-Allow-Origin"] "*")
                     (assoc-in [:headers "Access-Control-Allow-Methods"] "GET, POST")
                     (assoc-in [:headers "Access-Control-Allow-Headers"] "Origin, X-Requested-With, Content-Type, Accept")))
  :handle-ok (fn [{{qs :query-string} :request :as ctx}]

               (let [params (reduce (fn [c [k v]] (assoc c (keyword k) v)) {} (map #(split % "=") (split qs "&")))
                     data-sort (sort-by (apply comp (map keyword (split (:sort params) " "))) data )
                     data-sort-ord (if (= (:order params) "desc")   (reverse data-sort) data-sort)
                     ]
                 (clojure.pprint/pprint params)

                 {:total (count data) :rows  (take (Integer. (:limit params)) (drop (Integer. (:offset params)) data-sort-ord))}
                 )
)
  :allowed-methods [:get :post :options])
;;ring-response
(Integer. "3")
(take 2(drop 5 (range 0 10)))
(def handler
  (make-handler ["/" {"foo" foo
                      "documents" documents
                      ["foo2/" :id] foo2
                      "secret-key"  (resource
                                     :available-media-types ["text/html"]
                                     :handle-ok (fn [_] (totp/secret-key)))
                      ["totp/" :secret]  (resource
                                     :available-media-types ["text/html"]
                                     :handle-ok (fn [{{{secret :secret} :route-params} :request}] (totp/totp-token secret)))}]))

(comment "curl calls: should be made from eshell"

         => "/foo"
         curl -v -H "Accept: application/json" http://localhost:3000/foo
         curl -v -H "Accept: text/html" http://localhost:3000/foo


         => "/foo2/item1"
         curl -v -H "Accept: text/html" http://localhost:3000/foo2/item1
  )
