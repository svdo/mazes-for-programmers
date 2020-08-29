(ns spike.generate.binary-tree-test
  (:require [clojure.test :refer [deftest testing is]]
            [spike.grid :as grid]
            [spike.generate.binary-tree :as binary-tree]))

(deftest binary-tree
  (testing "carve using binary tree"
    (let [grid (binary-tree/carve (grid/create 3 3))]
      (is (seq (filter seq (map :links (flatten grid))))))))
