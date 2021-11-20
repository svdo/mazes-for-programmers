(ns spike.maze
  (:gen-class)
  (:require [spike.grid :as grid]
            #_[spike.generate.binary-tree :as binary-tree]
            [spike.generate.sidewinder :as sidewinder]
            #_[spike.render.ascii :as ascii]
            [spike.render.box :as box]
            [spike.solve.dijkstra :as dijkstra]
            [lanterna.terminal :as t]))

(defn -main
  [size-str & _]
  (let [size (read-string size-str)
        term (t/get-terminal :unix)
        grid (-> (grid/create size size)
                 ;; binary-tree/carve
                 sidewinder/carve
                 (dijkstra/assign-distances 0 0)
                 (dijkstra/mark-shortest-path [0 0] [(dec size) (dec size)]))]
    (t/in-terminal
     term
     (t/put-string term (box/to-str grid (comp str :dijkstra/distance)))
     (t/put-string term (box/to-str grid #(if (:dijkstra/on-shortest-path %) "X" " ")))
     (read-line))))
