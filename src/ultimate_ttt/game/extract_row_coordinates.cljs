(ns ultimate-ttt.game.extract-row-coordinates
  (:require [ultimate-ttt.game.board :as board]
            [math.combinatorics :as combo]))

(defn- horizontal-rows
  "Returns all of its horizontal rows."
  []
  (vec (partition 3 3 (board/all-cells))))

(defn- row-coordinates?
  "Checks if given coordinates are in the same row and if they cover all of it.
  Assumes that coordinates are distinct and within bounds of the board."
  [coordinates]
  (let [sorted-coordinates (sort coordinates)
        xs (map first sorted-coordinates)
        ys (map second sorted-coordinates)]
    (and
     (= 3 (count coordinates))
     (or
      (= xs ys)                           ; first diagonal
      (and (apply < xs) (apply > ys))     ; second diagonal
      (and (apply = ys) (apply < xs))     ; vertical rows
      (and (apply = xs) (apply < ys)))))) ; horizontal rows

(defn extract-row-coordinates
  "Extracts coordinates for all possible rows: horizontal, vertical, and diagonal."
  []
  (let [h-rows (horizontal-rows)]
    (->>
     h-rows
     (apply combo/cartesian-product)
     (filter #(row-coordinates? %))
     (into h-rows))))
