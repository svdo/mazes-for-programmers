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
        [show-colors set-show-colors] (hooks/use-state false)
        [show-distances set-show-distances] (hooks/use-state false)
        {:keys [from to distances]} (-> grid
                                        (dijkstra/find-longest-path starting-point))
        grid (dijkstra/mark-shortest-path distances from to)
        max-distance (apply max (flatten (grid/map-cells :dijkstra/distance grid)))]
    (<>
     (d/button {:on-click #(set-show-colors (not show-colors))} "Toggle colors")
     (d/button {:on-click #(set-show-distances (not show-distances))} "Toggle distances")
     ($ Grid {:grid grid
              :starting-point starting-point
              :set-starting-point set-starting-point
              :end-point to
              :content-fn (when show-distances
                            #(if (:dijkstra/on-shortest-path %) (:dijkstra/distance %) " "))
              :color-fn (when show-colors
                          #(let [distance (:dijkstra/distance %)
                                 intensity (- 1.0 (/ distance max-distance))
                                 dark (* 255.0 intensity)
                                 bright (+ 128.0 (* 127 intensity))]
                             {:red dark :green bright :blue dark :alpha 0.5}))}))))

(defn str->int [s]
  (let [i (js/parseInt s)]
    (when-not (js/isNaN i) i)))

(defnc App []
  (let [[size set-size] (hooks/use-state 10)
        grid (-> (grid/create size size)
                 sidewinder/carve)]
    (<>
     (d/h1 "Maze")
     (d/p
      (d/label {:for "size"} "Size:")
      (d/input {:id "size " :type "text" :value size :on-change #(when-let [new-size (-> % .-target .-value str->int)] (set-size (min 30 new-size)))}))
     ($ Maze {:grid grid}))))

(defn ^:export start
  []
  (rdom/render ($ App) (js/document.getElementById "app")))
