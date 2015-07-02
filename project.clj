(defproject liberator-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.9.2"]]
  :ring {:handler liberator-example.core/handler}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; totp support
                 [cylon "0.5.1"]
                 ;; restful
                 [liberator "0.10.0"]
                 ;; http server
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]

                 ;; bidirectional routing
                 [bidi "1.18.7" :exclusions [ring/ring-core org.clojure/data.json org.clojure/tools.reader]]
                 ;; html rendering
                 [hiccup "1.0.5"]])
