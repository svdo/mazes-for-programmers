(ns maze.cli
  (:gen-class)
  (:require [maze.grid :as grid]
            [maze.carve.binary-tree :as binary-tree]
            [maze.carve.sidewinder :as sidewinder]
            [maze.render.ascii :as ascii]
            [maze.render.box :as box]
            [maze.solve.dijkstra :as dijkstra]
            [lanterna.terminal :as t]))

(defn -main
  [size-str & _]
  (let [size (read-string size-str)
        term (t/get-terminal :unix)
        {:keys [from to distances]} (-> (grid/create size size)
                                        ;; binary-tree/carve
                                        sidewinder/carve
                                        dijkstra/find-longest-path)
        grid (dijkstra/mark-shortest-path distances from to)]
    (t/in-terminal
     term
     (t/put-string term (box/to-str grid (comp str :dijkstra/distance)))
     (t/put-string term (box/to-str grid #(if (:dijkstra/on-shortest-path %) "X" " ")))
     (read-line))))
