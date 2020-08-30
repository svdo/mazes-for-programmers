(ns spike.maze
  (:gen-class)
  (:require [spike.grid :as grid]
            #_[spike.generate.binary-tree :as binary-tree]
            [spike.generate.sidewinder :as sidewinder]
            #_[spike.render.ascii :as ascii]
            [spike.render.box :as box]))

(defn -main
  [size-str & _]
  (let [size (read-string size-str)]
    (-> (grid/create size size)
        ;; binary-tree/carve
        sidewinder/carve
        box/to-str
        println)))
