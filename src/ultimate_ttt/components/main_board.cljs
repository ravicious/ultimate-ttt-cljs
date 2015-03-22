(ns ultimate-ttt.components.main-board
  (:require [reagent.core :as reagent]
            [ultimate-ttt.components.board :as board-component]))


(defn- cursors-for-seq-in-atom [given-atom path]
  {:pre [(vector? (get-in @given-atom path))]}
  (let [seq-to-cursorify (get-in @given-atom path)
        count-of-seq (count seq-to-cursorify)]
    (vec
      (map #(reagent/cursor given-atom (conj path %)) (range count-of-seq)))))

(defn key-for-board [board-cursor]
  (apply str (:coordinates @board-cursor)))

(defn- key-for-row [row]
  (apply str (map key-for-board row)))

(defn- cell-component [board-cursor app-state]
  [:td [board-component/board board-cursor app-state]])

(defn- row-component [board-cursors app-state]
  [:tr
   (doall
     (for [board board-cursors]
       ^{:key (key-for-board board)} [cell-component board app-state]))])

(defn main-board [app-state]
  (let [board-cursors (cursors-for-seq-in-atom app-state [:boards])
        rows (partition 3 board-cursors)]
    [:table
     (doall
       (for [row rows]
         ^{:key (key-for-row row)} [row-component row app-state]))]))
