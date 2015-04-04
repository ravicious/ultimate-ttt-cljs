(ns ultimate-ttt.ui.helpers)

(defn cell-active? [board cell-index]
  (= 0 (get-in board [:cells cell-index])))

(defn board-active? [active-board-index board-index]
  (or
    (nil? active-board-index)
    (= active-board-index board-index)))

(defn board-and-cell-active? [active-board-index board-index board cell-index]
  (and
    (board-active? active-board-index board-index)
    (cell-active? board cell-index)))
