(ns ultimate-ttt.ui.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe
                                   register-sub]]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.ui.helpers :as h]))

(register-sub
  :current-owner
  (fn [db _]
    (reaction (:current-owner @db))))

(register-sub
  :main-board
  (fn [db _]
    (reaction (:main-board @db))))

(register-sub
  :main-board-cells-count
  (fn [db _]
    (let [main-board (reaction (:main-board @db))]
      (reaction (count (:cells @main-board))))))

(register-sub
  :board
  (fn [db [_ board-index]]
    (reaction (get-in @db [:boards board-index]))))

(register-sub
  :board-active?
  (fn [db [_ board-index]]
    (let [active-board-index (reaction (:active-board @db))
          board (reaction (get-in @db [:boards board-index]))]
      (reaction (h/board-active? @active-board-index board-index @board)))))

(register-sub
  :cell-activity-statuses
  (fn [db [_ board-index]]
    (let [active-board-index (reaction (:active-board @db))
          board (reaction (get-in @db [:boards board-index]))
          get-cell-status #(h/board-and-cell-active? @active-board-index
                                                     board-index
                                                     @board
                                                     %)]
      (->>
        (:cells @board)
        (map get-cell-status)
        vec
        reaction))))
