(ns maze.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [maze.grid :as grid]
            [maze.render.ascii :as ascii]))

(def grid-10-10 (grid/create 10 10))

(def open3x3 
  (-> (grid/create 3 3)
      (grid/link-cells [0 0] [0 1])
      (grid/link-cells [0 1] [0 2])
      (grid/link-cells [0 0] [1 0])
      (grid/link-cells [0 1] [1 1])
      (grid/link-cells [0 2] [1 2])

      (grid/link-cells [1 0] [1 1])
      (grid/link-cells [1 1] [1 2])
      (grid/link-cells [1 0] [2 0])
      (grid/link-cells [1 1] [2 1])
      (grid/link-cells [1 2] [2 2])

      (grid/link-cells [2 0] [2 1])
      (grid/link-cells [2 1] [2 2])))


(comment 
  (print (ascii/to-str open3x3)))

(deftest grid-test
  (testing "create a new grid"
    (is (= 20 (-> (grid/create 30 20) grid/height)))
    (is (= 30 (-> (grid/create 30 20) grid/width))))

  (testing "getting cells"
    (is (not (nil? (-> grid-10-10 (grid/get-cell 0 0)))))
    (is (nil? (-> grid-10-10 (grid/get-cell -1 0))))
    (is (nil? (-> grid-10-10 (grid/get-cell 0 -1))))
    (is (nil? (-> grid-10-10 (grid/get-cell 10 0))))
    (is (nil? (-> grid-10-10 (grid/get-cell 0 10)))))

  (testing "cells know their location"
    (is (= 2 (:row (-> grid-10-10 (grid/get-cell 2 3)))))
    (is (= 3 (:col (-> grid-10-10 (grid/get-cell 2 3))))))

  (testing "cells know their neighbors"
    (is (= [2 3] (:north (grid/get-cell grid-10-10 3 3))))
    (is (= [4 3] (:south (grid/get-cell grid-10-10 3 3))))
    (is (= [3 2] (:west (grid/get-cell grid-10-10 3 3))))
    (is (= [3 4] (:east (grid/get-cell grid-10-10 3 3))))
    (is (nil? (:north (grid/get-cell grid-10-10 0 3))))
    (is (nil? (:south (grid/get-cell grid-10-10 9 3))))
    (is (nil? (:west (grid/get-cell grid-10-10 3 0))))
    (is (nil? (:east (grid/get-cell grid-10-10 3 9)))))

  (testing "initially no links"
    (is (empty? (:links (-> grid-10-10 (grid/get-cell 1 1))))))

  (testing "creating a link is bidirectional"
    (let [updated-grid (grid/link-cells grid-10-10 [1 1] [1 2])]
      (is (and (= #{[1 2]}
                  (-> updated-grid
                      (grid/get-cell 1 1)
                      :links))
               (= #{[1 1]}
                  (-> updated-grid
                      (grid/get-cell 1 2)
                      :links))))))
  
  (testing "linked cells of an unreachable cell"
    (let [grid (grid/create 2 2)]
      (is (empty? (grid/linked-cells grid 0 0)))))
  
  (testing "linked cells of an open grid"
    (testing "center of 3x3"
      (let [grid open3x3]
        (is (= #{(grid/get-cell grid 0 1)
                 (grid/get-cell grid 1 0)
                 (grid/get-cell grid 1 2)
                 (grid/get-cell grid 2 1)}
               (grid/linked-cells grid 1 1))))))

  (testing "mapping over all cells"
    (is (= [[[0 0] [0 1]] [[1 0] [1 1]]]
           (grid/map-cells grid/coords (grid/create 2 2))))))
