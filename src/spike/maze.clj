(ns spike.maze
  (:gen-class)
  (:require [spike.grid :as grid]
            [spike.generate.binary-tree :as binary-tree]
            [spike.render.ascii :as ascii]))

(defn -main
  [size-str & _]
  (let [size (read-string size-str)]
    (-> (grid/create size size)
        binary-tree/carve
        ascii/to-str
        println)))
