(ns spike.render.ascii-test
  (:require [clojure.test :refer [deftest testing is]]
            [spike.grid :as grid]
            [spike.render.ascii :as ascii]))

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
               (ascii/to-str))))))
