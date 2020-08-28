(ns spike.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [spike.grid :as grid]))

(deftest grid-test
  (testing "create a new grid"
    (is (grid/create 10 10))
    (is (= 20 (-> (grid/create 30 20) grid/height)))
    (is (= 30 (-> (grid/create 30 20) grid/width))))

  (testing "getting cells"
    (is (not (nil? (-> (grid/create 10 10) (grid/cell 0 0)))))
    (is (nil? (-> (grid/create 10 10) (grid/cell -1 0))))
    (is (nil? (-> (grid/create 10 10) (grid/cell 0 -1))))
    (is (nil? (-> (grid/create 10 10) (grid/cell 10 0))))
    (is (nil? (-> (grid/create 10 10) (grid/cell 0 10))))))
