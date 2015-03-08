(defproject ultimate-ttt "0.1.0-SNAPSHOT"
  :description "Ultimate Tic-Tac-Toe written in ClojureScript"
  :url "https://github.com/ravicious/ultimate-ttt-cljs"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3058"]
                 [figwheel "0.2.5-SNAPSHOT"]]

  :node-dependencies [[source-map-support "0.2.8"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-figwheel "0.2.5-SNAPSHOT"]
            [lein-npm "0.4.0"]
            [com.cemerick/clojurescript.test "0.3.3"]]

  :source-paths ["src" "target/classes"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "out-test"]

  :cljsbuild {
              :builds [{:id "dev"
                        :source-paths ["src"]
                        :compiler {
                                   :main ultimate-ttt.core
                                   :asset-path "js/compiled/out-dev"
                                   :output-to "resources/public/js/compiled/ultimate_ttt.js"
                                   :output-dir "resources/public/js/compiled/out-dev"
                                   :optimizations :none
                                   :cache-analysis true
                                   :source-map true}}
                       {:id "release"
                        :source-paths ["src"]
                        :compiler {
                                   :main ultimate-ttt.core
                                   :asset-path "js/compiled/out-production"
                                   :output-to "resources/public/js/compiled/ultimate_ttt.min.js"
                                   :output-dir "resources/public/js/compiled/out-production"
                                   :optimizations :advanced
                                   :pretty-print false}}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :notify-command ["phantomjs" :cljs.test/runner "out-test/ultimate_ttt.test.js"]
                        :compiler {
                                   :output-to "out-test/ultimate_ttt.test.js"
                                   :output-dir "out-test"
                                   :optimizations :whitespace
                                   :pretty-print true
                                   :cache-analysis true}}]
              :test-commands {"test" ["phantomjs" :runner "out-test/ultimate_ttt.test.js"]}})
