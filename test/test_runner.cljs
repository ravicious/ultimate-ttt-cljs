(ns test-runner
  (:require [cljs.test :refer-macros [run-tests]]
            [ultimate-ttt.game.board-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'ultimate-ttt.game.board-test))
    0
    1))
