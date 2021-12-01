(ns maze.render.ascii-test
  (:require [clojure.test :refer [deftest testing is]]
            [maze.grid :as grid]
            [maze.render.ascii :as ascii]))

(deftest ascii-test
  (testing "to ascii"
    (is (= "
+---+---+
|   |   |
+---+---+
|   |   |
+---+---+
"
           (-> (grid/create 2 2) (ascii/to-str))))
    (is (= "
+---+---+
|   |   |
+---+---+
|       |
+---+---+
"
           (-> (grid/create 2 2)
               (grid/link-cells [1 0] [1 1])
               (ascii/to-str))))
    (is (= "
+---+---+
|   |   |
+---+   +
|   |   |
+---+---+
"
           (-> (grid/create 2 2)
               (grid/link-cells [0 1] [1 1])
               (ascii/to-str))))

    (is (= "
+---+---+
| 0 | 1 |
+---+   +
| 2 | 3 |
+---+---+
"
           (-> (grid/create 2 2 :value (fn [row col] (+ (* 2 row) col)))
               (grid/link-cells [0 1] [1 1])
               (ascii/to-str (comp str :value)))))
    ))
