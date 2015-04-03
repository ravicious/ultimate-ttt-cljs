(ns ultimate-ttt.ui.view-helpers)

(defn owner->player [owner]
  (case owner
    1 "X"
    2 "O"
    "-"))

