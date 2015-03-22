(ns ultimate-ttt.board-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [ultimate-ttt.board :as b]))

;;; Here we modify the cells so that each value is equal to its index.
;;; It makes testing get-cell method easier.
(def board
  (let [new-board (b/init-board)
        cells-size (count (:cells new-board))]
    (reduce #(assoc-in %1 [:cells %2] %2) new-board (range cells-size))))

(deftest init-board
  (testing "number of cells equals to the squared board size"
    (is (= 16 (count (:cells (b/init-board 4))))))
  (testing "default size is 3"
    (is (= 3 (b/size (b/init-board))))))

(deftest within-bounds?
  (testing "first cell"
    (is (b/within-bounds? board 0 0)))
  (testing "last cell"
    (is (b/within-bounds? board 2 2)))
  (testing "position equal to board size"
    (is (not (b/within-bounds? board 3 3)))))

(deftest get-cell
  (testing "first cell"
    (is (= 0 (b/get-cell board 0 0))))
  (testing "last cell"
    (is (= 8 (b/get-cell board 2 2))))
  (testing "middle cell"
    (is (= 4 (b/get-cell board 1 1))))
  (testing "out of bounds cell"
    (is (thrown? js/Error (b/get-cell board 4 5)))))

(deftest set-cell
  (testing "setting cell"
    (let [modified-board (b/set-cell board 0 0 2)]
      (is (= 2 (b/get-cell modified-board 0 0)))))
  (testing "setting an invalid owner"
    (is (thrown-with-msg? js/Error #"Assert failed" (b/set-cell board 0 0 4)))))

(deftest calculate-coordinates
  (is (= '(1 1) (b/calculate-coordinates board 4)))
  (is (= '(2 0) (b/calculate-coordinates board 6))))
