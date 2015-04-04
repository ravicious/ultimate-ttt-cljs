(ns ultimate-ttt.ui.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch]]
            [ultimate-ttt.game.board :as board-helpers]
            [ultimate-ttt.game.referee :as referee]
            [ultimate-ttt.ui.helpers :as h]))

(defn- valid-move? [db board-index cell-index]
  (let [active-board-index (:active-board db)
        main-board (:main-board db)
        board (get-in db [:boards board-index])
        game-in-progress? (not (:winner db))]
    (and
      (h/board-and-cell-active? main-board active-board-index board-index board cell-index)
      game-in-progress?)))

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
  (fn [db [_ next-board-index]]
    (let [main-board (:main-board db)]
      (if (h/cell-active? main-board next-board-index) ; check if the next board hasn't been finished yet
        (assoc db :active-board next-board-index)
        (assoc db :active-board nil)))))

(defn update-main-board
  "Checks if the game on a minor board has been finished and reflects its status on the main board"
  [db [_ board-index]]
  (let [board (get-in db [:boards board-index])
        winner (referee/find-winner board)]
    (if winner
      (let [main-board (:main-board db)
            updated-main-board (board-helpers/set-cell main-board board-index winner)
            main-board-winner (referee/find-winner updated-main-board)]
        (->
          db
          (assoc :main-board updated-main-board)
          (assoc :winner main-board-winner)))
      db)))

(register-handler :update-main-board update-main-board)

(defn- cell-clicked [db [_ board-index cell-index]]
  (when (valid-move? db board-index cell-index)
    (let [current-owner (:current-owner db)]
      (dispatch [:change-cell-owner board-index cell-index current-owner])
      (dispatch [:update-main-board board-index])
      (dispatch [:change-current-owner current-owner])
      (dispatch [:change-active-board cell-index])))
  db)

(register-handler :cell-clicked cell-clicked)
