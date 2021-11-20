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
