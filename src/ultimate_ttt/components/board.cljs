(ns ultimate-ttt.components.board
  (:require [reagent.core :as reagent]
            [ultimate-ttt.board :as board-helper]
            [ultimate-ttt.view-helpers :as view-helpers]))

(defn- key-for-cell [cell]
  (:index cell))

(defn- key-for-row [row]
  (apply str (map key-for-cell row)))

(defn- cell-on-click [index board-cursor app-state]
  (let [board-record (:board @board-cursor)
        coordinates (board-helper/calculate-coordinates board-record index)
        current-owner (:current-owner @app-state)
        new-board (board-helper/set-cell board-record (first coordinates) (second coordinates) current-owner)]
    (swap! board-cursor assoc :board new-board)
    (swap! app-state assoc :current-owner 2))) ; TODO: add cycling the current-owner

(defn- cell-component [{:keys [owner index board-cursor] :as cell} app-state]
  [:td
   {:on-click #(cell-on-click index board-cursor app-state)}
   (view-helpers/owner->player owner)])

(defn- row-component [cells app-state]
  [:tr
   (doall
     (for [cell cells]
       ^{:key (key-for-cell cell)} [cell-component cell app-state]))])

(defn board [board-cursor app-state]
  (let [board-record (:board @board-cursor)
        board-size (board-helper/size board-record)
        raw-cells (map vector (:cells board-record) (range) (repeat board-cursor))
        cells (map #(zipmap [:owner :index :board-cursor] %) raw-cells)
        rows (partition 3 cells)]
    [:table
     (doall
       (for [row rows]
         ^{:key (key-for-row row)} [row-component row app-state]))]))
