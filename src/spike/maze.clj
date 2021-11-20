(ns spike.maze
  (:gen-class)
  (:require [spike.grid :as grid]
            [spike.generate.binary-tree :as binary-tree]
            [spike.generate.sidewinder :as sidewinder]
            [spike.render.ascii :as ascii]
            [spike.render.box :as box]
            [spike.solve.dijkstra :as dijkstra]
            [lanterna.terminal :as t]))

(defn -main
  [size-str & _]
  (let [size (read-string size-str)
        term (t/get-terminal :unix)
        from [(quot size 2) (quot size 2)]
        to   [(dec size) (dec size)]
        grid (-> (grid/create size size)
                 ;; binary-tree/carve
                 sidewinder/carve
                 (dijkstra/assign-distances from)
                 (dijkstra/mark-shortest-path from to))]
    (t/in-terminal
     term
     (t/put-string term (box/to-str grid (comp str :dijkstra/distance)))
     (t/put-string term (box/to-str grid #(if (:dijkstra/on-shortest-path %) "X" " ")))
     (read-line))))
