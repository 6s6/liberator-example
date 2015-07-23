(defproject liberator-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.9.2"]]

  :ring {:handler liberator-example.core/handler
         :init liberator-example.core/init
         :destroy liberator-example.core/destroy}

  :aot :all
  :dependencies [[org.clojure/clojure "1.7.0"]
                 ;; totp support
                 [tangrammer/cylon "0.5.0" :exclusions [com.stuartsierra/component org.clojure/clojure malcolmsparks/co-dependency]]
                 ;; restful
                 [liberator "0.13"]
                 ;; http server
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]

                 ;; bidirectional routing
                 [bidi "1.18.7" :exclusions [ring/ring-core org.clojure/data.json org.clojure/tools.reader]]
                 ;; html rendering
                 [hiccup "1.0.5"]
                   [com.google.appengine/appengine-api-1.0-sdk "1.9.15"]])
