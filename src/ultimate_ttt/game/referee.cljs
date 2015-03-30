(ns ultimate-ttt.game.referee
  (:require [ultimate-ttt.game.board :as b]
            [ultimate-ttt.game.extract-row-coordinates :refer [extract-row-coordinates]]))

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
  (->>
   (b/size board)
   (extract-row-coordinates)
   (map #(b/coordinates->owners board %))
   (select-winner)))
