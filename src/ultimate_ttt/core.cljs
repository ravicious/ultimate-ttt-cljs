(ns ^:figwheel-always ultimate-ttt.core
  (:require [figwheel.client :as fw]
            [reagent.core :as reagent :refer [atom]]
            [ultimate-ttt.board :as board-helpers]
            [ultimate-ttt.referee :as referee]
            [ultimate-ttt.view-helpers :as view-helpers]
            [ultimate-ttt.components.main-board :as main-board]))

(enable-console-print!)

(println "Ready.")

(fw/start {
           :on-jsload (fn [] (print "reloaded"))})

(defn- init-boards []
  (let [boards (repeatedly #(board-helpers/init-board 3))
        coordinates (board-helpers/all-cells 3)]
    (->>
      (map list boards coordinates)
      (map #(zipmap [:board :coordinates] %))
      (vec))))

(defonce app-state (atom {:main-board (board-helpers/init-board)
                          :current-owner 1
                          :boards (init-boards)}))

(defn game-progress [main-board current-owner]
  (let [winner (referee/find-winner main-board)
        current-player (view-helpers/owner->player current-owner)]
    [:div
     [:p (if
           (nil? winner)
           (str current-player "s turn")
           (str (view-helpers/owner->player winner) "s won!"))]]))

(defn root []
  [:div
   [game-progress (:main-board @app-state) (:current-owner @app-state)]
   [main-board/main-board app-state]])

(reagent/render [root] (. js/document (getElementById "app")))
