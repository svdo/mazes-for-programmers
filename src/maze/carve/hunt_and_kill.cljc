(ns maze.carve.hunt-and-kill 
  (:require [maze.grid :as grid]))

(defn unvisited-neighbors
  [grid cell]
  (->> cell
       grid/neighbors
       (filter #(empty? (grid/linked-cells grid %)))
       (map (partial grid/get-cell grid))))

(defn visited-neighbors
  [grid cell]
  (->> cell
       grid/neighbors
       (filter #(seq (grid/linked-cells grid %)))
       (map (partial grid/get-cell grid))))

(defn carve [grid]
  (loop [grid grid
         current (grid/random-cell grid)]
    (let [unvisited-neighbors (unvisited-neighbors grid current)]
      (if (seq unvisited-neighbors)
        (let [neighbor (rand-nth unvisited-neighbors)]
          (recur (grid/link-cells grid (grid/coords current) (grid/coords neighbor))
                 neighbor))
        (if-let [[cell neighbor] (some->> (grid/all-cells grid)
                                          (map (juxt identity (partial visited-neighbors grid)))
                                          (filter (fn [[cell visited-neighbors]]
                                                    (and (empty? (grid/linked-cells grid (grid/coords cell)))
                                                         (seq visited-neighbors))))
                                          first
                                          ((fn [[cell visited-neighbors]]
                                             [cell (rand-nth visited-neighbors)])))]
          (recur (grid/link-cells grid (grid/coords cell) (grid/coords neighbor))
                 cell)
          grid)))))
