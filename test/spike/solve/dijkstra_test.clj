(ns spike.solve.dijkstra-test
  (:require [clojure.test :refer [deftest is testing]]
            [spike.grid :as grid]
            [spike.render.ascii :as ascii]
            [spike.solve.dijkstra :as dijkstra]))

(def grid1
  (-> (grid/create 2 2)
      (grid/link-cells [0 0] [0 1])))

(comment
  (print (ascii/to-str grid1))
  ;; +---+---+
  ;; |       |
  ;; +---+---+
  ;; |   |   |
  ;; +---+---+
  )

(def grid2 
  (-> (grid/create 3 3)
      (grid/link-cells [0 0] [0 1])
      (grid/link-cells [0 1] [1 1])
      (grid/link-cells [1 1] [1 2])
      (grid/link-cells [1 2] [0 2])
      (grid/link-cells [1 1] [1 0])
      (grid/link-cells [1 0] [2 0])
      (grid/link-cells [2 0] [2 1])
      (grid/link-cells [2 1] [2 2])))

(comment 
  (print (ascii/to-str grid2))
  ;; +---+---+---+
  ;; |       |   |
  ;; +---+   +   +
  ;; |           |
  ;; +   +---+---+
  ;; |           |
  ;; +---+---+---+
  )

(deftest dijkstra-test
  (testing "starts at zero"
    (is (= 0 (-> (grid/create 1 1)
                 (dijkstra/assign-distances 0 0)
                 (grid/get-cell 0 0)
                 :dijkstra/distance))))
  
  (testing "grid1"
    (is (= [[0 1] [nil nil]]
           (-> grid1
               (dijkstra/assign-distances 0 0)
               ((partial grid/map-cells :dijkstra/distance))))))
  
  (testing "shortest path"
    (is (= [[true true] [nil nil]]
           (-> grid1
               (dijkstra/assign-distances 0 0)
               (dijkstra/mark-shortest-path [0 0] [0 1])
               ((partial grid/map-cells :dijkstra/on-shortest-path))))))
  
  (testing "shortest path 2"
    (is (= [[true true nil] [true true nil] [true true true]]
           (-> grid2
               (dijkstra/assign-distances 0 0)
               (dijkstra/mark-shortest-path [0 0] [2 2])
               ((partial grid/map-cells :dijkstra/on-shortest-path))))))
  )

(comment

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


  (let [grid (-> open3x3
                 (assoc-in  [0 0 :dijkstra/distance] 0))
        [g f] (dijkstra/assign-distances-to-frontier grid #{(grid/get-cell grid 0 0)} 1)
        [g f] (dijkstra/assign-distances-to-frontier g f 2)
        [g f] (dijkstra/assign-distances-to-frontier g f 3)
        [g f] (dijkstra/assign-distances-to-frontier g f 4)
        ;; 
        ]
    (-> g
        (ascii/to-str (comp str :dijkstra/distance))
        print)
    (map grid/coords f))

  (let [grid (-> open3x3 (assoc-in  [0 0 :dijkstra/distance] 0))]
    (-> grid
        (dijkstra/assign-distances 0 0)
        (ascii/to-str (comp str :dijkstra/distance))
        print))

  (let [grid (-> grid1 (assoc-in  [0 0 :dijkstra/distance] 0))]
    (-> grid
        (dijkstra/assign-distances 0 0)
        (ascii/to-str (comp str :dijkstra/distance))
        print))
  
  (-> open3x3
      (dijkstra/assign-distances 0 0)
      (dijkstra/neighbor-with-lowest-distance [1 1]))
  
  (sort-by :distance #{{:distance 1} {:distance 5} {:distance 2}})
  )
