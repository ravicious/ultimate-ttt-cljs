(ns ultimate-ttt.ui.components.board
  (:require [reagent.core :as reagent]
            [ultimate-ttt.game.board :as board-helper]
            [ultimate-ttt.ui.view-helpers :as view-helpers]
            [re-frame.core :refer [subscribe dispatch]]))

(defn- key-for-cell [cell]
  (:cell-index cell))

(defn- key-for-row [row]
  (apply str "row" (map key-for-cell row)))

(defn- cell-component [{:keys [owner cell-index board-index] :as cell}]
  [:td.minor-board--cell
   {:on-click #(dispatch [:cell-clicked board-index cell-index])}
   (view-helpers/owner->player owner)])

(defn- row-component [cells]
  [:tr.minor-board--row
   (doall
     (for [cell cells]
       ^{:key (key-for-cell cell)} [cell-component cell]))])

(defn board [board-index]
  (let [board-record (subscribe [:board board-index])]
    (fn []
      (let [board-size (board-helper/size @board-record)
            raw-cells (map vector (:cells @board-record) (range) (repeat board-index))
            cells (map #(zipmap [:owner :cell-index :board-index] %) raw-cells)
            rows (partition 3 cells)]
        [:table.minor-board--table
         (doall
           (for [row rows]
             ^{:key (key-for-row row)} [row-component row]))]))))