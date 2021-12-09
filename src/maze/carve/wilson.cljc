(ns maze.carve.wilson
  (:require [maze.grid :as grid]
            [clojure.set :as set]))

(defn- loop-erased-random-walk [grid unvisited]
  (loop [cell (rand-nth (seq unvisited))
         path [cell]
         grid grid]
    (if (contains? unvisited cell)
      (let [random-neighbor-coords (grid/random-neighbor-coordinate cell)
            neighbor (grid/get-cell grid random-neighbor-coords)
            index-in-path (.indexOf path neighbor)
            path (if (<= 0 index-in-path)
                   (take (inc index-in-path) path)
                   (concat path [neighbor]))]
        (recur neighbor path grid))
      path)))

(defn- link-cells [grid [cell1 cell2]]
  (cond-> grid
    (and cell1 cell2)
    (grid/link-cells (grid/coords cell1) (grid/coords cell2))))

(defn carve [grid]
  (loop [cell (grid/random-cell grid)
         unvisited (-> (set (flatten grid))
                       (disj cell))
         grid grid]
    (if-not (empty? unvisited)
      (let [path (loop-erased-random-walk grid unvisited)
            grid (reduce link-cells grid (map (fn [a b] [a b]) (butlast path) (rest path)))
            unvisited (set/difference unvisited (set path))]
        (recur (rand-nth (seq unvisited)) unvisited grid))
      grid)))
