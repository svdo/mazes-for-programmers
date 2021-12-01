(ns maze.core
  (:require [helix.core :as hx :refer [$ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [maze.lib :refer [defnc]]
            ["react-dom" :as rdom]
            [spike.generate.sidewinder :as sidewinder]
            [spike.grid :as grid]
            [spike.render.helix :refer [Grid]]
            [spike.solve.dijkstra :as dijkstra]))

(defnc Maze [{:keys [grid]}]
  (let [[starting-point set-starting-point] (hooks/use-state [0 0])
        {:keys [from to distances]} (-> grid
                                        (dijkstra/find-longest-path starting-point))
        grid (dijkstra/mark-shortest-path distances from to)
        max-distance (apply max (flatten (grid/map-cells :dijkstra/distance grid)))]
    ($ Grid {:grid grid
             :set-starting-point set-starting-point
             :content-fn
             #_(comp str :dijkstra/distance)
             #(if (:dijkstra/on-shortest-path %) (:dijkstra/distance %) " ")
             :color-fn #(let [distance (:dijkstra/distance %)
                              intensity (- 1.0 (/ distance max-distance))
                              dark (* 255.0 intensity)
                              bright (+ 128.0 (* 127 intensity))]
                          {:red dark :green bright :blue dark :alpha 0.5})})))

(defnc App []
  (let [[size set-size] (hooks/use-state 10)
        grid (-> (grid/create size size)
                 sidewinder/carve)]
    (<>
     (d/h1 "Maze")
     (d/p
      (d/label {:for "size"} "Size:")
      (d/input {:id "size " :type "text" :value size :on-change #(set-size (min 30 (-> % .-target .-value)))}))
     ($ Maze {:grid grid}))))

(defn ^:export start
  []
  (rdom/render ($ App) (js/document.getElementById "app")))
