(ns ultimate-ttt.ui.components.main-board
  (:require [reagent.core :as reagent]
            [ultimate-ttt.ui.components.board :as board-component]
            [re-frame.core :refer [subscribe]]))

(defn key-for-board [board-index]
  (str "board" board-index))

(defn- key-for-row [row]
  (apply str "row" row))

(defn- cell-component [board-index]
  [:td [board-component/board board-index]])

(defn- row-component [board-indexes]
  [:tr
   (doall
     (for [board-index board-indexes]
       ^{:key (key-for-board board-index)} [cell-component board-index]))])

(defn main-board []
  (let [main-board-cells-count (subscribe [:main-board-cells-count])]
    (fn []
      (let [rows (partition 3 (range @main-board-cells-count))]
        [:table
         (doall
           (for [row rows]
             ^{:key (key-for-row row)} [row-component row]))]))))
