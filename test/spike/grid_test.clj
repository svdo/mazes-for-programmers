(ns spike.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [spike.grid :as grid]))

(def grid-10-10 (grid/create 10 10))

(deftest grid-test
  (testing "create a new grid"
    (is (= 20 (-> (grid/create 30 20) grid/height)))
    (is (= 30 (-> (grid/create 30 20) grid/width))))

  (testing "getting cells"
    (is (not (nil? (-> grid-10-10 (grid/cell 0 0)))))
    (is (nil? (-> grid-10-10 (grid/cell -1 0))))
    (is (nil? (-> grid-10-10 (grid/cell 0 -1))))
    (is (nil? (-> grid-10-10 (grid/cell 10 0))))
    (is (nil? (-> grid-10-10 (grid/cell 0 10)))))

  (testing "cells know their location"
    (is (= 2 (:row (-> grid-10-10 (grid/cell 2 3)))))
    (is (= 3 (:col (-> grid-10-10 (grid/cell 2 3))))))

  (testing "initially no links"
    (is (empty? (:links (-> grid-10-10 (grid/cell 1 1))))))

  (testing "creating a link is bidirectional"
    (is (let [updated-grid (grid/link-cells grid-10-10 [1 1] [1 2])]
          (and (= #{(grid/cell grid-10-10 1 2)}
                  (-> updated-grid
                      (grid/cell 1 1)
                      :links))
               (= #{(grid/cell grid-10-10 1 1)}
                  (-> updated-grid
                      (grid/cell 1 2)
                      :links)))))))
