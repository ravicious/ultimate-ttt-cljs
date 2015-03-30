(ns ultimate-ttt.game.extract-row-coordinates-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [ultimate-ttt.game.extract-row-coordinates :as erc]))

(deftest extract-rows
  (testing "all possible rows for a board of size two"
    (is (=
         (sort
          '(((0 0) (0 1))
            ((0 0) (1 1))
            ((0 0) (1 0))
            ((0 1) (1 0))
            ((0 1) (1 1))
            ((1 0) (1 1))))
         (sort (erc/extract-row-coordinates 2)))))
  (testing "hand-picked rows for a board of size three"
    (let [rows (erc/extract-row-coordinates 3)]
      (is (some #{'((0 1) (1 1) (2 1))} rows))
      (is (not (some #{'((0 0) (0 0) (2 0))} rows))))))
