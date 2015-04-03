(ns ^:figwheel-always ultimate-ttt.core
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   dispatch
                                   subscribe]]
            [ultimate-ttt.ui.subscriptions]
            [ultimate-ttt.ui.handlers]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.game.referee :as referee]
            [ultimate-ttt.ui.view-helpers :as view-helpers]
            [ultimate-ttt.ui.components.main-board :as main-board]))

(enable-console-print!)

(println "Ready.")

(defonce initial-state {:main-board (board-helpers/init-board)
                        :current-owner 1
                        :boards (vec (repeat 9 (board-helpers/init-board)))
                        :active-board nil
                        :database-initialized? true})

(register-handler
  :initialize
  (fn [db _]
    (if (:database-initialized? db)
      db
      initial-state)))

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
