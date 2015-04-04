(ns ultimate-ttt.ui.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe
                                   register-sub]]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.game.referee :as referee]
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
  :main-board-winner
  (fn [db _]
    (let [main-board (reaction (:main-board @db))]
      (reaction (referee/find-winner @main-board)))))

(register-sub
  :board
  (fn [db [_ board-index]]
    (reaction (get-in @db [:boards board-index]))))

(register-sub
  :board-active?
  (fn [db [_ board-index]]
    (let [active-board-index (reaction (:active-board @db))
          main-board (reaction (:main-board @db))]
      (reaction (h/board-active? @main-board @active-board-index board-index)))))

(register-sub
  :cell-activity-statuses
  (fn [db [_ board-index]]
    (let [active-board-index (reaction (:active-board @db))
          main-board (reaction (:main-board @db))
          board (reaction (get-in @db [:boards board-index]))
          board-active? (reaction (h/board-active? @main-board @active-board-index board-index))
          get-cell-status #(and
                             @board-active?
                             (h/cell-active? @board %))]
      (->>
        (board-helpers/size @board)
        board-helpers/all-indexes
        (map get-cell-status)
        vec
        reaction))))
