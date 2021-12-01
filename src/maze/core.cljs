(ns maze.core
  (:require [helix.core :as hx :refer [$ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [maze.lib :refer [defnc]]
            ["react-dom" :as rdom]
            [maze.carve.sidewinder :as sidewinder]
            [maze.carve.binary-tree :as binary-tree]
            [spike.grid :as grid]
            [maze.render.helix :refer [Grid]]
            [maze.solve.dijkstra :as dijkstra]))

(defnc Maze [{:keys [grid]}]
  (let [[starting-point set-starting-point] (hooks/use-state [0 0])
        [show-colors set-show-colors] (hooks/use-state false)
        [show-distances set-show-distances] (hooks/use-state false)
        [animation-index set-animation-index] (hooks/use-state nil)
        {:keys [from to distances intermediates]} (-> grid
                                                      (dijkstra/find-longest-path-keep-dinstances starting-point))
        grid (dijkstra/mark-shortest-path distances from to)
        max-distance (apply max (flatten (grid/map-cells :dijkstra/distance grid)))]
    (<>
     (d/span {:display "inline-block"
              :style {:margin-right "1em"}}
             (d/input {:id "show-colors"
                       :type "checkbox"
                       :checked show-colors
                       :on-change #(set-show-colors (not show-colors))})
             (d/label {:for "show-colors"} "show colors"))
     (d/span {:display "inline-block"
              :style {:margin-right "1em"}}
             (d/input {:id "show-distances"
                       :type "checkbox"
                       :checked show-distances
                       :on-change #(set-show-distances (not show-distances))})
             (d/label {:for "show-distances"} "show distances"))
     (d/button {:on-click #(set-animation-index (if (nil? animation-index) 0 nil))} (if (nil? animation-index) "Start animate" "Stop animate"))
     (when-not (nil? animation-index)
       (d/button {:on-click #(let [new-index ((fnil inc -1) animation-index)
                                   new-index (when (<= new-index max-distance) new-index)]
                               (set-animation-index new-index))} ">>"))
     (let [grid (if (nil? animation-index) grid (nth intermediates animation-index))
           normal-content-fn (when show-distances
                               #(if (:dijkstra/on-shortest-path %) (:dijkstra/distance %) " "))
           animating-content-fn #(:dijkstra/distance %)
           content-fn (if (nil? animation-index) normal-content-fn animating-content-fn)]
       ($ Grid {:grid grid
                :starting-point starting-point
                :set-starting-point set-starting-point
                :end-point to
                :content-fn content-fn
                :color-fn (when show-colors
                            #(let [distance (:dijkstra/distance %)
                                   intensity (- 1.0 (/ distance max-distance))
                                   dark (* 255.0 intensity)
                                   bright (+ 128.0 (* 127 intensity))]
                               {:red dark :green bright :blue dark :alpha 0.5}))})))))

(defn str->int [s]
  (let [i (js/parseInt s)]
    (when-not (js/isNaN i) i)))

(defnc App []
  (let [[size set-size] (hooks/use-state 10)
        [carve-algo set-carve-algo] (hooks/use-state "sidewinder")
        _ (js/console.debug carve-algo)
        grid (-> (grid/create size size)
                 ((case carve-algo
                    "binary-tree" binary-tree/carve
                    "sidewinder" sidewinder/carve)))]
    (<>
     (d/h1 "Maze")
     (d/p
      (d/label {:for "size" :style {:margin-right "0.5em"}} "Size:")
      (d/input {:id "size" :type "text" :value size :on-change #(when-let [new-size (-> % .-target .-value str->int)] (set-size (min 30 new-size)))}))
     (d/p 
      (d/label {:for "carve-algo" :style {:margin-right "0.5em"}} "Carve:")
      (d/select {:id "carve-algo"
                 :value carve-algo
                 :on-change (fn [e] (set-carve-algo (-> e .-target .-value)))}
                (d/option {:value "sidewinder"} "Sidewinder")
                (d/option {:value "binary-tree"} "Binary tree")))
     ($ Maze {:grid grid}))))

(defn ^:export start
  []
  (rdom/render ($ App) (js/document.getElementById "app")))
