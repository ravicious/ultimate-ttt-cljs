(ns test-runner
  (:require [cljs.test :refer-macros [run-tests]]
            [ultimate-ttt.board-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'ultimate-ttt.board-test))
    0
    1))
