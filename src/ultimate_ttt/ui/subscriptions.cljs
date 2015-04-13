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
  :main-board-winner
  (fn [db _]
    (reaction (:winner @db))))

(register-sub
  :board
  (fn [db [_ board-index]]
    (reaction (get-in @db [:boards board-index]))))

(register-sub
  :board-active?
  (fn [db [_ board-index]]
    (let [active-board-index (reaction (:active-board @db))
          main-board (reaction (:main-board @db))
          winner (reaction (:winner @db))]
      (reaction (and
                  (h/board-active? @main-board @active-board-index board-index)
                  (not @winner))))))

(register-sub
  :winning-board?
  (fn [db [_ board-index]]
    (let [main-board (reaction (:main-board @db))
          winner (reaction (:winner @db))
          board-owner (reaction (get-in @main-board [:cells board-index]))]
      (reaction (and
                  @winner
                  (= @winner @board-owner))))))

(register-sub
  :cell-activity-statuses
  (fn [db [_ board-index]]
    (let [winner (reaction (:winner @db))
          board (reaction (get-in @db [:boards board-index]))]
      (if @winner
        (reaction (vec (take 9 (repeat false))))
        (let [active-board-index (reaction (:active-board @db))
              main-board (reaction (:main-board @db))
              board-active? (reaction (h/board-active? @main-board @active-board-index board-index))
              get-cell-status #(and
                                 @board-active?
                                 (h/cell-active? @board %))]
          (->>
            (board-helpers/all-indexes)
            (map get-cell-status)
            vec
            reaction))))))
