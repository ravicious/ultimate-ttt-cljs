(ns ^:figwheel-always ultimate-ttt.core
  (:require [figwheel.client :as fw]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ultimate-ttt.board :as board-helpers]
            [ultimate-ttt.referee :as referee]
            [ultimate-ttt.view-helpers :as view-helpers]
            [ultimate-ttt.components.board :as board-component]))

(enable-console-print!)

(println "Ready.")

(fw/start {
           :on-jsload (fn [] (print "reloaded"))})

(defonce app-state (atom {:main-board (board-helpers/init-board) :current-owner 1}))

(defn game-progress [data owner]
  (reify
    om/IRender
    (render [_]
      (let [main-board (:main-board data)
            winner (view-helpers/owner->player (referee/find-winner main-board))
            current-player (view-helpers/owner->player (:current-owner data))]
        (dom/div
          nil
          (dom/p
            nil
            (if
              (nil? winner)
              (str current-player "s turn")
              (str winner "s won!"))))))))

(om/root
  (fn [data owner]
    (reify
      om/IRender
      (render [_]
        (let [main-board (:main-board data)
              winner (referee/find-winner main-board)]
          (dom/div
            nil
            (dom/h1 nil "Ultimate TTT")
            (om/build game-progress data)
            (om/build board-component/board-component main-board))))))
  app-state
  {:target (. js/document (getElementById "app"))})
