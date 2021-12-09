(ns maze.carve.aldous-broder-test
  (:require [clojure.test :refer [deftest testing is]]
            [maze.carve.aldous-broder :as aldous-broder]
            [maze.grid :as grid]))

(deftest aldous-broder-test
  (testing "carve using Aldous-Broder"
    (let [grid (aldous-broder/carve (grid/create 3 3))]
      (is (seq (filter seq (map :links (flatten grid))))))))
