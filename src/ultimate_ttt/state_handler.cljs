(ns ultimate-ttt.state-handler
  (:require [reagent.core :as reagent :refer [atom]]
            [ultimate-ttt.board :as board-helper]))

(defn- init-boards []
  (let [boards (repeatedly #(board-helper/init-board 3))
        coordinates (board-helper/all-cells 3)]
    (->>
      (map list boards coordinates)
      (map #(zipmap [:board :coordinates] %))
      (vec))))

(defonce app-state (atom {:main-board (board-helper/init-board)
                          :current-owner 1
                          :boards (init-boards)}))

(defn- choose-next-owner []
  (let [current-owner (:current-owner @app-state)]
    (if (= current-owner 1) 2 1)))

(defn- change-current-owner []
  (swap! app-state assoc :current-owner (choose-next-owner)))

(defn- change-cell-owner [index board-cursor]
  (let [board-record (:board @board-cursor)
        coordinates (board-helper/calculate-coordinates board-record index)
        current-owner (:current-owner @app-state)
        new-board (board-helper/set-cell board-record (first coordinates) (second coordinates) current-owner)]
    (swap! board-cursor assoc :board new-board)))

(defn on-cell-click [index board-cursor]
  (do
    (change-cell-owner index board-cursor)
    (change-current-owner)))
