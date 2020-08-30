(ns spike.generate.sidewinder-test
  (:require [clojure.test :refer [deftest testing is]]
            [spike.grid :as grid]
            [spike.generate.sidewinder :as sidewinder]))

(deftest sidewinder
  (testing "carve using sidewinder"
    (let [grid (sidewinder/carve (grid/create 3 3))]
      (is (seq (filter seq (map :links (flatten grid))))))))
