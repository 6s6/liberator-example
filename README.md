# liberator-example

A Clojure library designed to ... well, that part is up to you.

## Usage

```

lein ring uberjar
java -jar target/liberator-example-0.1.0-SNAPSHOT-standalone.jar  &

$ lein repl

 (use 'ring.adapter.jetty)
 (use 'liberator-example.core)

 (def s (run-jetty handler {:port 3003 :join? false}))
 (.stop s)
```
OR
`lein ring server-headless 3003 &`

update version 

liberator-example-0.1.0-SNAPSHOT-standalone.war
;; uncomment :aot :all in project.clj to deploy 
lein clean && lein ring uberwar && unzip target/liberator-example-0.1.0-SNAPSHOT-standalone.war -d target/war && cp appengine-web.xml target/war/WEB-INF/ && appcfg.sh --oauth2 update target/war/


## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.



