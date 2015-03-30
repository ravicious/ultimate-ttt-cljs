(ns ultimate-ttt.interface.state-handler
  (:require [reagent.core :as reagent :refer [atom]]
            [ultimate-ttt.game.board :as board-helper]))

#_(
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

(defn- set-active-board [index]
  (swap! app-state assoc :active-board (get-in @app-state [:boards index :board])))

(defn is-active? [board]
  (let [active-board (:active-board @app-state)]
    (or
      (nil? active-board)
      (= board active-board))))

(defn on-cell-click [index board-cursor]
  (let [board-record (:board @board-cursor)]
    (when (is-active? board-record)
      (change-cell-owner index board-cursor)
      (change-current-owner)
      (set-active-board index))))
)
