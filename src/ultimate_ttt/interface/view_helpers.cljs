(ns ultimate-ttt.interface.view-helpers)

(defn owner->player [owner]
  (case owner
    1 "X"
    2 "O"
    "-"))

