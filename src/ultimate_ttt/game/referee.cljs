(ns ultimate-ttt.game.referee
  (:require [ultimate-ttt.game.board :as b]))

(def ^{:private true
       :doc "All rows (horizontal, vertical, diagonal) of a TTT board of size 3."}
  row-coordinates
  '[((0 0) (0 1) (0 2)) ((1 0) (1 1) (1 2)) ((2 0) (2 1) (2 2)) ((0 0) (1 0) (2 0)) ((0 0) (1 1) (2 2)) ((0 1) (1 1) (2 1)) ((0 2) (1 1) (2 0)) ((0 2) (1 2) (2 2))])

(defn- any-row-won-by-player?
  "Given rows of cell owners, determines if any row has been won by player x"
  [cell-owners x]
  ; row that is won by player x is going to be filled only with xs
  (->>
   cell-owners
   (map distinct)         ; remove duplicated values from rows
   (map #(= (list x) %))  ; check if any row contains only the value x
   (some #{true})))

(defn- select-winner [row-sums]
  (cond
    (any-row-won-by-player? row-sums 1) 1
    (any-row-won-by-player? row-sums 2) 2
    :else nil))

(defn find-winner [board]
  "Given a board, returns the winning owner or nil. Be careful, it's a very expensive operation!"
  (->>
    row-coordinates
   (map #(b/coordinates->owners board %))
   (select-winner)))
