(ns ultimate-ttt.game.board)

(def ^:private allowed-owners #{1 2})

(defrecord Board [cells])

(defn within-bounds? [& coordinates]
  (let [sorted-coordinates (sort coordinates)
        upper-bound 2 ; we assume the max size of the board is 3, so 2 is the max coordinate value
        lower-bound 0
        bounds-with-coordinates (list lower-bound sorted-coordinates upper-bound)]
    (apply <= (flatten bounds-with-coordinates))))

(defn- init-cells
  "Creates a list of cells with no owner"
  []
  (vec (take 9 (repeat 0))))

(defn init-board []
  (Board. (init-cells)))

(defn- calculate-index
  "Translates 2D coordinates to an index in one-dimensional array"
  [board x y]
  {:pre [(within-bounds? x y)]}
  (+ y (* 3 x)))

(defn calculate-coordinates
  "Translates 1D index to 2D coordinates"
  [board i]
  {:post [(apply within-bounds? %)]}
  (list (quot i 3) (mod i 3)))

(defn get-cell
  "Given a board and coordinates, returns the cell owner"
  ([board x y]
   (get-cell board (calculate-index board x y)))
  ([board index]
   (get-in board [:cells index])))

(defn set-cell
  "Given a board and coordinates, sets a new cell owner"
  ([board x y owner]
   (let [index (calculate-index board x y)]
     (set-cell board index owner)))
  ([board index owner]
   {:pre [(allowed-owners owner)]}
   (assoc-in board [:cells index] owner)))

(defn coordinates->owners [board coordinates]
  (map #(get-cell board (first %) (second %)) coordinates))

(defn all-cells
  "Returns coordinates for all possible cells in a board."
  []
  (for [x (range 0 3)
        y (range 0 3)]
    (list x y)))

(defn all-indexes
  "Returns indexes for all possible cells in a board."
  []
  (range 9))

