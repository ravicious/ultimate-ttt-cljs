(ns ultimate-ttt.ui.helpers)

(defn cell-active? [board cell-index]
  (= 0 (get-in board [:cells cell-index])))

(defn board-active? [main-board active-board-index board-index]
  (and
    (or
      (nil? active-board-index)
      (= active-board-index board-index))
    (cell-active? main-board board-index)))

(defn board-and-cell-active? [main-board active-board-index board-index board cell-index]
  (and
    (board-active? main-board active-board-index board-index)
    (cell-active? board cell-index)))
