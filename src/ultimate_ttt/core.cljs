(ns ^:figwheel-always ultimate-ttt.core
  (:require [figwheel.client :as fw]
            [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   dispatch
                                   subscribe]]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.game.referee :as referee]
            [ultimate-ttt.interface.view-helpers :as view-helpers]
            [ultimate-ttt.interface.components.main-board :as main-board]
            [ultimate-ttt.interface.state-handler :as state-handler]
            [ultimate-ttt.interface.subscriptions]))

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

(defonce initial-state {:main-board (board-helpers/init-board)
                        :current-owner 1
                        :boards (init-boards)
                        :active-board nil})

(register-handler
  :initialize
  (fn [_ _] initial-state))

(dispatch [:initialize])

(defn game-progress []
  (let [main-board (subscribe [:main-board])
        current-owner (subscribe [:current-owner])]
    (fn[]
      (let [winner (referee/find-winner @main-board)
            current-player (view-helpers/owner->player @current-owner)]
        [:div
         [:p (if
               (nil? winner)
               (str current-player "s turn")
               (str (view-helpers/owner->player winner) "s won!"))]]))))

(defn root []
  [:div
   [game-progress]
   [main-board/main-board]])

(reagent/render [root] (. js/document (getElementById "app")))
