(ns liberator-example.core
  (:require [liberator.core :refer (resource defresource)]
            [ring.middleware.params :refer (wrap-params)]
            [hiccup.core :refer (html)]
            [bidi.ring :refer (make-handler)]
            [cylon.user.totp :as totp]))

(defn current-time []
  (format "It's %d milliseconds since the beginning of the epoch" (System/currentTimeMillis)))

(defresource foo
  :available-media-types ["text/html" "application/json"]
  :allowed-methods [:get :post ]
  :handle-ok (fn [ctx]
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

(def handler
  (make-handler ["/" {"foo" foo
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
