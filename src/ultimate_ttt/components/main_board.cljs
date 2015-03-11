(ns ultimate-ttt.components.main-board
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ultimate-ttt.components.board :as board-component]))

(defn- minor-board-component [{:keys [board coordinates]} owner]
  (reify
    om/IRender
    (render [_]
      (dom/td
        nil
        (om/build board-component/board board)))))

(defn- row-component [boards-and-coords owner]
  (reify
    om/IRender
    (render [_]
      (apply dom/tr nil
             (map #(om/build minor-board-component %) boards-and-coords)))))

(defn main-board [{:keys [board boards-and-coords]} owner]
  (reify
    om/IDisplayName
    (display-name [_] "Main board")
    om/IRender
    (render [_]
      (let [rows (partition 3 boards-and-coords)]
        (apply dom/table nil
               (map #(om/build row-component %) rows))))))
