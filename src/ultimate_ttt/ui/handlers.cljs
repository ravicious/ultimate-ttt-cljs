(ns ultimate-ttt.ui.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch]]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.game.referee :as referee]
            [ultimate-ttt.ui.helpers :as h]))

(defn- valid-move? [db board-index cell-index]
  (let [active-board-index (:active-board db)
        main-board (:main-board db)
        board (get-in db [:boards board-index])]
    (h/board-and-cell-active? main-board active-board-index board-index board cell-index)))

(register-handler
  :change-cell-owner
  (fn [db [_ board-index cell-index current-owner]]
    (let [board (get-in db [:boards board-index])
          updated-board (board-helpers/set-cell board cell-index current-owner)]
      (assoc-in db [:boards board-index] updated-board))))

(register-handler
  :change-current-owner
  (fn [db [_ current-owner]]
    (let [next-owner (if (= current-owner 1) 2 1)]
      (assoc db :current-owner next-owner))))

(register-handler
  :change-active-board
  (fn [db [_ clicked-cell-index]]
    (let [next-board (get-in db [:boards clicked-cell-index])]
      (if-not (referee/find-winner next-board)
        (assoc db :active-board clicked-cell-index)
        (assoc db :active-board nil)))))

(defn updated-main-board
  "Checks if the game on a minor board has been finished and reflects its status on the main board"
  [db [_ board-index current-owner]]
  (let [board (get-in db [:boards board-index])
        winner (referee/find-winner board)]
    (if winner
      (let [main-board (:main-board db)
            updated-main-board (board-helpers/set-cell main-board board-index current-owner)]
        (assoc db :main-board updated-main-board))
      db)))

(register-handler :update-main-board updated-main-board)

(defn- cell-clicked [db [_ board-index cell-index]]
  (when (valid-move? db board-index cell-index)
    (let [current-owner (:current-owner db)]
      (dispatch [:change-cell-owner board-index cell-index current-owner])
      (dispatch [:update-main-board board-index current-owner])
      (dispatch [:change-current-owner current-owner])
      (dispatch [:change-active-board cell-index])))
  db)

(register-handler :cell-clicked cell-clicked)
