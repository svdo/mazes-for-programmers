(ns maze.core
  (:require [helix.core :as hx :refer [$ <>]]
            [helix.dom :as d]
            [maze.lib :refer [defnc]]
            ["react-dom" :as rdom]
            [spike.generate.sidewinder :as sidewinder]
            [spike.grid :as grid]
            [spike.render.helix :refer [Grid]]
            [spike.solve.dijkstra :as dijkstra]))

(defnc App []
  (let [{:keys [from to distances]} (-> (grid/create 3 3)
                                        sidewinder/carve
                                        dijkstra/find-longest-path)
        grid (dijkstra/mark-shortest-path distances from to)]
    (<>
     (d/h1 "Maze" )
     ;; (d/pre (ascii/to-str grid (comp str :dijkstra/distance)))
     ($ Grid {:grid grid :content-fn 
              #_(comp str :dijkstra/distance)
              #(if (:dijkstra/on-shortest-path %) (:dijkstra/distance %) " ")}))))

(defn ^:export start
  []
  (rdom/render ($ App) (js/document.getElementById "app")))
