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
            [ultimate-ttt.interface.subscriptions]
            [ultimate-ttt.interface.handlers]))

(enable-console-print!)

(println "Ready.")

(fw/start {:on-jsload (fn [] (print "reloaded"))})

(defonce initial-state {:main-board (board-helpers/init-board)
                        :current-owner 1
                        :boards (vec (repeat 9 (board-helpers/init-board)))
                        :active-board nil})

(register-handler
  :initialize
  (fn [_ _] initial-state))

(dispatch [:initialize])

(defn game-progress []
  (let [main-board (subscribe [:main-board])
        current-owner (subscribe [:current-owner])]
    (fn []
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
