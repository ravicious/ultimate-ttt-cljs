(ns ultimate-ttt.board-component
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ultimate-ttt.board :as board-helper]
            [ultimate-ttt.extract-row-coordinates :as extract-coordinates]
            [ultimate-ttt.view-helpers :as view-helpers]))

(defn- cell-component [[[x y] cell-owner] owner]
  (reify
    om/IRender
    (render [_]
      (dom/td
        nil
        (dom/span
          nil
          (or
            (view-helpers/owner->player cell-owner)
            "–"))))))

(defn- row-component [row-cells owner]
  (reify
    om/IRender
    (render [_]
      (apply dom/tr nil
             (map #(om/build cell-component %) row-cells)))))

(defn board-component [board owner]
  (reify
    om/IRender
    (render [_]
      (let [board-size (board-helper/size board)
            coordinates (extract-coordinates/all-cells board-size)
            owners (board-helper/coordinates->owners board coordinates)
            coords-and-owner (map vector coordinates owners)
            rows (partition 3 coords-and-owner)]
        (apply dom/table nil
               (map #(om/build row-component %) rows))))))
