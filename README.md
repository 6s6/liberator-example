# liberator-example

A Clojure library designed to ... well, that part is up to you.

## Usage

```

lein ring uberjar
java -jar target/liberator-example-0.1.0-SNAPSHOT-standalone.jar  &

$ lein repl

 (use 'ring.adapter.jetty)
 (use 'liberator-example.core)
 (def s (run-jetty handler))

```
OR
`lein ring server-headless 3003 &`

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.



