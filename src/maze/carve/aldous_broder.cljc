(ns maze.carve.aldous-broder
  (:require [maze.grid :as grid]))

(defn- random-neighbor-coordinate [cell]
  (->> cell
       ((juxt :north :south :east :west))
       (filter some?)
       rand-nth))

(defn carve
  [grid]
  (loop [unvisited-count (dec (grid/size grid))
         cell (grid/random-cell grid)
         grid grid]
    (if (< 0 unvisited-count)
      (let [random-neighbor-coords (random-neighbor-coordinate cell)
            neighbor (grid/get-cell grid random-neighbor-coords)
            linked-cells (grid/linked-cells grid random-neighbor-coords)]
        (if (empty? linked-cells)
          (recur (dec unvisited-count)
                 neighbor
                 (grid/link-cells grid (grid/coords cell) random-neighbor-coords))
          (recur unvisited-count
                 neighbor
                 grid)))
      grid)))

(comment
  (tap> ((juxt :north :south :east :west) (first (flatten (grid/create 3 3)))))
  )
