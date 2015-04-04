(ns ultimate-ttt.ui.helpers
  (:require [ultimate-ttt.game.referee :as referee]))

(defn cell-active? [board cell-index]
  (= 0 (get-in board [:cells cell-index])))

(defn board-active? [active-board-index board-index board]
  (and
    (or
      (nil? active-board-index)
      (= active-board-index board-index))
    (not (referee/find-winner board))))

(defn board-and-cell-active? [active-board-index board-index board cell-index]
  (and
    (board-active? active-board-index board-index board)
    (cell-active? board cell-index)))
