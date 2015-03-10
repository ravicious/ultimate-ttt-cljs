(ns ultimate-ttt.view-helpers)

(defn owner->player [owner]
  (case owner
    1 "X"
    2 "O"
    nil))

