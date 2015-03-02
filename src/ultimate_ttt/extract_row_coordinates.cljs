(ns ultimate-ttt.extract-row-coordinates
  (:require [math.combinatorics :as combo]))

(defn- all-cells
  "Given a board size, returns coordinates for all possible cells in a board."
  [board-size]
  (for [x (range 0 board-size)
        y (range 0 board-size)]
    (list x y)))

(defn- horizontal-rows
  "Given a board size, returns all of its horizontal rows."
  [board-size]
  (partition board-size board-size (all-cells board-size)))

(defn- row-coordinates?
  "Checks if given coordinates are in the same row and if they cover all of it.
  Assumes that coordinates are distinct and within bounds of the board."
  [board-size coordinates]
  (let [sorted-coordinates (sort coordinates)
        xs (map first sorted-coordinates)
        ys (map second sorted-coordinates)]
    (and
     (= board-size (count coordinates))
     (or
      (= xs ys)                           ; first diagonal
      (and (apply < xs) (apply > ys))     ; second diagonal
      (and (apply = ys) (apply < xs))     ; vertical rows
      (and (apply = xs) (apply < ys)))))) ; horizontal rows

(defn extract-row-coordinates
  "Given a board size, extracts coordinates for all possible rows: horizontal, vertical, and diagonal."
  [board-size]
  (let [h-rows (horizontal-rows board-size)]
    (->>
     h-rows
     (apply combo/cartesian-product)
     (filter #(row-coordinates? board-size %))
     (into h-rows))))
