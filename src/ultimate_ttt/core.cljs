(ns ^:figwheel-always ultimate-ttt.core
  (:require [figwheel.client :as fw]
            [reagent.core :as reagent :refer [atom]]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.game.referee :as referee]
            [ultimate-ttt.interface.view-helpers :as view-helpers]
            [ultimate-ttt.interface.components.main-board :as main-board]
            [ultimate-ttt.interface.state-handler :as state-handler]))

(enable-console-print!)

(println "Ready.")

(fw/start {
           :on-jsload (fn [] (print "reloaded"))})

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
   [game-progress (:main-board @state-handler/app-state) (:current-owner @state-handler/app-state)]
   [main-board/main-board state-handler/app-state]])

(reagent/render [root] (. js/document (getElementById "app")))
