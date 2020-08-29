(ns spike.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [spike.grid :as grid]))

(def grid-10-10 (grid/create 10 10))

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
    (is (let [updated-grid (grid/link-cells grid-10-10 [1 1] [1 2])]
          (and (= #{[1 2]}
                  (-> updated-grid
                      (grid/get-cell 1 1)
                      :links))
               (= #{[1 1]}
                  (-> updated-grid
                      (grid/get-cell 1 2)
                      :links)))))))
