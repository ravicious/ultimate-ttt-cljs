(ns ultimate-ttt.game.referee-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)]
                   [clojure.test.check.clojure-test
                    :refer (defspec)])
  (:require [cemerick.cljs.test :as t]
            [ultimate-ttt.game.board :as b]
            [ultimate-ttt.game.referee :as r]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop :include-macros true]
            [clojure.test.check.clojure-test :as ct]))

;;; Helpers & generators for tests

(def random-board-gen (gen/fmap (fn [cells]
                                  (let [board (b/init-board)]
                                    (assoc board :cells cells)))
                                (gen/vector (gen/choose 0 2) 9)))

(def rows {:horizontal [[0 1 2] [3 4 5] [6 7 8]]
           :vertical [[0 3 6] [1 4 7] [2 5 8]]
           :diagonal [[0 4 8] [2 4 6]]})

(defn row->owners [board indexes]
  (map #(b/get-cell board %) indexes))

(defn rows->owners [board rows]
  (->>
    rows
    (map #(row->owners board %))))

(defn any-consecutive? [board dimension]
  (->>
    (dimension rows)
    (rows->owners board)
    (some #{[1 1 1] [2 2 2]})))

;;; Actual tests

(defspec board-with-less-than-three-marks-has-no-winner
  100
  (prop/for-all [board random-board-gen]
                (let [freqs (frequencies (:cells board))]
                  (if (and
                          (< (freqs 1) 3)
                          (< (freqs 2) 3))
                    (not (r/find-winner board))
                    true))))

(defspec board-with-three-horizontal-marks-has-a-winner
  100
  (prop/for-all [board random-board-gen]
                (if (any-consecutive? board :horizontal)
                  (r/find-winner board)
                  true)))

(defspec board-with-three-vertical-marks-has-a-winner
  100
  (prop/for-all [board random-board-gen]
                (if (any-consecutive? board :vertical)
                  (r/find-winner board)
                  true)))

(defspec board-with-three-diagonal-marks-has-a-winner
  100
  (prop/for-all [board random-board-gen]
                (if (any-consecutive? board :diagonal)
                  (r/find-winner board)
                  true)))
