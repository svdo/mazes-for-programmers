(ns maze.carve.wilson-test
  (:require [clojure.test :refer [deftest testing is]]
            [maze.carve.wilson :as wilson]
            [maze.grid :as grid]))

(deftest wilson-test
  (testing "carve using Wilson's"
    (let [grid (wilson/carve (grid/create 3 3))]
      (is (seq (filter seq (map :links (flatten grid))))))))
