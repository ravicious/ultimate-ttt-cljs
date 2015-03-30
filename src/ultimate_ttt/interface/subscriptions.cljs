(ns ultimate-ttt.interface.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe
                                   register-sub]]
            [ultimate-ttt.game.board :as board-helpers]))

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
    (reaction (:board (nth (:boards @db) board-index)))))
