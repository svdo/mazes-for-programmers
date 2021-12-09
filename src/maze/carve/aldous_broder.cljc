(ns maze.carve.aldous-broder
  (:require [maze.grid :as grid]))

(defn carve
  [grid]
  (loop [unvisited-count (dec (grid/size grid))
         cell (grid/random-cell grid)
         grid grid]
    (if (< 0 unvisited-count)
      (let [random-neighbor-coords (grid/random-neighbor-coordinate cell)
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
