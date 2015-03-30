(ns ultimate-ttt.game.referee-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [ultimate-ttt.game.board :as b]
            [ultimate-ttt.game.referee :as r]))

(def board-won-by-player-1
  (let [new-board (b/init-board)]
    (->
     new-board
     (b/set-cell 0 0 1)
     (b/set-cell 1 1 1)
     (b/set-cell 2 2 1))))

(def board-won-by-player-2
  (let [new-board (b/init-board)]
    (->
     new-board
     (b/set-cell 0 2 2)
     (b/set-cell 1 2 2)
     (b/set-cell 2 2 2))))

(def unfinished-board
  (let [new-board (b/init-board)]
    (->
     new-board
     (b/set-cell 0 0 1)
     (b/set-cell 1 2 2)
     (b/set-cell 0 2 1))))

(deftest find-winner
  (testing "finding a winner for a won board"
    (is (= 1 (r/find-winner board-won-by-player-1)))
    (is (= 2 (r/find-winner board-won-by-player-2))))
  (testing "finding a winner for an unfinished board"
    (is (= nil (r/find-winner unfinished-board)))))
