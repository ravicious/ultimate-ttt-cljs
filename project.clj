(defproject ultimate-ttt "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2755"] ]

  :node-dependencies [[source-map-support "0.2.8"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-npm "0.4.0"] ]

  :source-paths ["src" "target/classes"]

  :clean-targets ["out" "out-adv" "out-test"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {
                                   :main ultimate-ttt.core
                                   :output-to "out/ultimate_ttt.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :cache-analysis true
                                   :source-map true}}
                       {:id "release"
                        :source-paths ["src"]
                        :compiler {
                                   :main ultimate-ttt.core
                                   :output-to "out-adv/ultimate_ttt.min.js"
                                   :output-dir "out-adv"
                                   :optimizations :advanced
                                   :pretty-print false}}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :notify-command ["phantomjs" "phantom/unit-test.js" "phantom/unit-test.html"]
                        :compiler {
                                   ; :main ultimate-ttt.core
                                   :output-to "out-test/ultimate_ttt.test.js"
                                   :output-dir "out-test"
                                   :optimizations :whitespace
                                   :pretty-print true}}]
              :test-commands {"test" ["phantomjs" "phantom/unit-test.js" "phantom/unit-test.html"]}})
