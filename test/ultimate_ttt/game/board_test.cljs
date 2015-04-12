(ns ultimate-ttt.game.board-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)]
                   [clojure.test.check.clojure-test
                    :refer (defspec)])
  (:require [cemerick.cljs.test :as t]
            [ultimate-ttt.game.board :as b]
            [ultimate-ttt.game.referee :as referee]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop :include-macros true]
            [clojure.test.check.clojure-test :as ct]))

;;; Here we modify the cells so that each value is equal to its index.
;;; It makes testing get-cell method easier.
(def board
  (let [new-board (b/init-board)
        cells-size (count (:cells new-board))]
    (reduce #(assoc-in %1 [:cells %2] %2) new-board (range cells-size))))

(deftest init-board
  (testing "number of cells equals to the squared board size"
    (is (= 9 (count (:cells (b/init-board))))))
  (testing "all cells have no owner"
    (is (= [0] (distinct (:cells (b/init-board)))))))

(deftest within-bounds?
  (testing "first cell"
    (is (b/within-bounds? 0 0)))
  (testing "last cell"
    (is (b/within-bounds? 2 2)))
  (testing "position equal to board size"
    (is (not (b/within-bounds? 3 3)))))

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

(defn ascending?
  [coll]
  (every? (fn [[a b]] (<= a b))
          (partition 2 1 coll)))

(defspec testing-out-double-check
  100
  (prop/for-all [v (gen/vector gen/int)]
                (let [s (sort v)]
                  (and (= (count v) (count s))
                       (ascending? s)))))

;; rozmiar boardu (strictly positive int)
;; index (choose 0 rozmiar-boardu)
;; owner (one-of [1 2])
;; (size, [index, owner], [index, owner], [index, owner])

; (def board-gen (gen/fmap b/init-board size-gen))

(def owners-gen (gen/shuffle [1 2]))

(def size-gen (gen/choose 2 6))
(def board-arguments-gen
  ^{:doc "Returns a tuple containing the board size,
         shuffled list of owners to cycle and a list of moves"}
  (gen/bind size-gen
            #(gen/tuple
               (gen/return %)
               (gen/shuffle [1 2])
               (gen/fmap vec
                         (gen/fmap distinct
                                   (gen/vector
                                     (gen/tuple
                                       (gen/choose 0 (dec %))
                                       (gen/choose 0 (dec %)))))))))

(defn make-move [board [[x y] owner]]
  (b/set-cell board x y owner))

(defn valid-move? [board x y]
  (and
    (= 0 (b/get-cell board x y))
    (not (referee/find-winner board))))

(defn make-moves [board moves-and-owners]
  (if-let [[[x y] owner] (first moves-and-owners)]
    (if (valid-move? board x y)
      (recur (b/set-cell board x y owner) (rest moves-and-owners))
      board)
    board))

(def board-gen (gen/fmap (fn [[size owners moves]]
                           (let [board (b/init-board size)
                                 moves-and-owners (map vector moves (cycle owners))]
                             (make-moves board moves-and-owners)))
                         board-arguments-gen))


(def board-full-gen (gen/tuple size-gen ))

(def my-gen (gen/vector (gen/tuple (gen/choose 0 6) (gen/choose 7 9))))
