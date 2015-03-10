(ns ultimate-ttt.board)

(def ^:private allowed-owners #{1 2})

(defrecord Board [cells])

(defn size [{cells :cells}]
  (Math/sqrt (count cells)))

(defn within-bounds? [board & coordinates]
  (let [sorted-coordinates (sort coordinates)
        board-size (size board)
        upper-bound (dec board-size)
        lower-bound 0
        bounds-with-coordinates (list lower-bound sorted-coordinates upper-bound)]
    (apply <= (flatten bounds-with-coordinates))))

(defn- init-cells
  "Creates a list of cells with no owner"
  [board-size]
  (vec (take (* board-size board-size) (repeat 0))))

(defn init-board
  ([]
   (init-board 3))
  ([board-size]
   (Board. (init-cells board-size))))

(defn- calculate-index
  "Translates 2D coordinates to an index in one-dimensional array"
  [board x y]
  {:pre [(within-bounds? board x y)]}
  (let [board-size (size board)]
    (+ y (* board-size x))))

(defn get-cell
  "Given a board and coordinates, returns the cell owner"
  [{cells :cells, :as board} x y]
  (nth cells (calculate-index board x y)))

(defn set-cell
  "Given a board and coordinates, sets a new cell owner"
  [{cells :cells, :as board} x y owner]
  {:pre [(allowed-owners owner)]}
  (let [index (calculate-index board x y)]
    (assoc-in board [:cells index] owner)))

(defn coordinates->owners [board coordinates]
  (map #(get-cell board (first %) (second %)) coordinates))

