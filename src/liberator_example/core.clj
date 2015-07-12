(ns liberator-example.core
  (:require [liberator.core :refer (resource defresource)]
            [liberator.representation :refer (ring-response as-response)]
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


(def data [{:id 1 :left {:region "Europe", :country"Spain", :countryCode"ESP"},
            :middle {:title "ESP-Diagnostic Trade Integration Study - Espain",
                     :description "The document analyzes the current economic situation and the determinants of poverty in Angola, and it outlines policy actions in ten main areas of intervention: 1) probation; 2) demining; 3) food security and rural development; 4) HIV/AIDS; 5) Education; 6) Health; 7) Basic infrastructures; 8) professional training and employment; 9) governance; 10) macroeconomic governance. \n
The DTIS provides an overview of the current economic situation in Angola and of the main issues regarding poverty and trade. It analyses the key problems affecting infrastructures, trade regime and institutions, commercial barriers, trade facilitation and private sector development. \n
The Programme of Cooperation is aligned with the Common Country Programme Document, the National Strategy for Development and Integration 2007-2013 and the Millennium Development Goals. Hence, the goal of the Programme is to promote fair and sustainable development, social inclusion, respect of international standards and obligations in light of the integration of Albania into the European Union. Specific expected outcomes are identified within four main areas of intervention: governance and rule of law, economy and environment, regional and local development, inclusive and social policy.",
                     :sectors["Coffee", "Cashew","Farming" , "Lorem", "sector1", "sector2", "sector3", "sector4", "sector5", "sector6", "sector7", "sector8","Coffee", "Cashew","Farming" , "Lorem", "sector1", "sector2", "sector3", "sector4", "sector5", "sector6", "sector7", "sector8"]
                     },
            :right {:type "UNAAF",:year 2003, :implementationPeriod "2007-2010",:lastUpdate"Thu May 28 2015"}}
           {:id 2 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2005, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 3 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2006, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 4 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 5 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 6 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 7 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 8 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 9 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 10 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }
           {:id 11 :left {:region "Africa", :country"Benin", :countryCode"BEN"},
            :middle {:title "Diagnostic Trade Integration Study - Benin",
                     :description "Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism,Agro-Processing Industry, Cashew, Cotton, Fisheries, Pineapple, Shea, Shrimp Farming,Tourism", :sectors["Coffee", "Cashew","Farming" , "Lorem"]
                     },
            :right {:type "UNAAF",
                    :year 2007, :implementationPeriod"2008-2012",:lastUpdate"Fry Jul 13 2012"}
            }

           ])


(defresource documents
  :available-media-types ["application/json"]
  :as-response (fn [d ctx]
                 (-> (as-response d ctx) ;; default implementation
                     (assoc-in [:headers "Access-Control-Allow-Origin"] "*")
                     (assoc-in [:headers "Access-Control-Allow-Methods"] "GET, POST")
                     (assoc-in [:headers "Access-Control-Allow-Headers"] "Origin, X-Requested-With, Content-Type, Accept")))
  :handle-ok (fn [{{qs :query-string} :request :as ctx}]

               (clojure.pprint/pprint (reduce (fn [c [k v]] (assoc c k v)) {} (map #(split % "=") (split qs "&"))))
                data

)
  :allowed-methods [:get :post :options])
;;ring-response
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
