(ns maze.render.box-test
  (:require [clojure.test :refer [deftest testing is]]
            [maze.grid :as grid]
            [maze.render.box :as box]))

(deftest box-test
  (testing "to box"
    (is (= "
┌───┬───┐
│   │   │
├───┼───┤
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2) (box/to-str))))
    (is (= "
┌───┬───┐
│   │   │
├───┴───┤
│       │
└───────┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [1 0] [1 1])
               (box/to-str))))
    (is (= "
┌───────┐
│       │
├───┬───┤
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [0 1])
               (box/to-str))))
    (is (= "
┌───────┐
│       │
├───────┤
│       │
└───────┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [0 1])
               (grid/link-cells [1 0] [1 1])
               (box/to-str))))
    (is (= "
┌───┬───┐
│   │   │
├───┤   │
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 1] [1 1])
               (box/to-str))))
    (is (= "
┌───┬───┐
│   │   │
│   ├───┤
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [1 0])
               (box/to-str))))
    (is (= "
┌───┬───┐
│   │   │
│   │   │
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [1 0])
               (grid/link-cells [0 1] [1 1])
               (box/to-str))))
    (is (= "
┌───┬───┐
│   │   │
│   └───┤
│       │
└───────┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [1 0])
               (grid/link-cells [1 0] [1 1])
               (box/to-str))))
    (is (= "
┌───────┐
│       │
│   ┌───┤
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [1 0])
               (grid/link-cells [0 0] [0 1])
               (box/to-str))))
    (is (= "
┌───┬───┐
│   │   │
├───┘   │
│       │
└───────┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 1] [1 1])
               (grid/link-cells [1 0] [1 1])
               (box/to-str))))
    (is (= "
┌───────┐
│       │
├───┐   │
│   │   │
└───┴───┘
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 0] [0 1])
               (grid/link-cells [0 1] [1 1])
               (box/to-str))))))
