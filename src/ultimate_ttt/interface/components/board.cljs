(ns ultimate-ttt.interface.components.board
  (:require [reagent.core :as reagent]
            [ultimate-ttt.game.board :as board-helper]
            [ultimate-ttt.interface.view-helpers :as view-helpers]
            [ultimate-ttt.interface.state-handler :as state-handler]))

(defn- key-for-cell [cell]
  (:index cell))

(defn- key-for-row [row]
  (apply str (map key-for-cell row)))

(defn- cell-component [{:keys [owner index board-cursor] :as cell}]
  [:td.minor-board--cell
   {:on-click #(state-handler/on-cell-click index board-cursor)}
   (view-helpers/owner->player owner)])

(defn- row-component [cells]
  [:tr.minor-board--row
   (doall
     (for [cell cells]
       ^{:key (key-for-cell cell)} [cell-component cell]))])

(defn board [board-cursor]
  (let [board-record (:board @board-cursor)
        board-size (board-helper/size board-record)
        raw-cells (map vector (:cells board-record) (range) (repeat board-cursor))
        cells (map #(zipmap [:owner :index :board-cursor] %) raw-cells)
        rows (partition 3 cells)]
    [:table.minor-board--table
     (doall
       (for [row rows]
         ^{:key (key-for-row row)} [row-component row]))]))
