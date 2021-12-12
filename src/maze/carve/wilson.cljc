(ns maze.carve.wilson
  (:require [maze.grid :as grid]
            [clojure.set :as set]))

(defn- loop-erased-next-cell
  "This is the normal loop-erased version of Wilson's."
  [cell path grid _unvisited]
  (let [random-neighbor-coords (grid/random-neighbor-coordinate cell)
        neighbor (grid/get-cell grid random-neighbor-coords)
        index-in-path (.indexOf path neighbor)
        path (if (<= 0 index-in-path)
               (take (inc index-in-path) path)
               (concat path [neighbor]))]
    {:updated-path path :next-cell neighbor}))

(defn- loop-avoiding-next-cell
  "This alternative of `loop-erased-next-cell` implements a variant
   where it avoids creating loops. This causes the maze to be partitioned
   into segments that have no path to eachother."
  [cell path grid _unvisited] 
  (let [neighbor-coords (grid/neighbors cell)
        neighbors (map (partial grid/get-cell grid) neighbor-coords)
        neighbors-not-on-path (filter #(< (.indexOf path %) 0) neighbors)]
    (when (seq neighbors-not-on-path)
      (let [random-neighbor-not-on-path (rand-nth neighbors-not-on-path)
            path (concat path [random-neighbor-not-on-path])]
        {:updated-path path :next-cell random-neighbor-not-on-path}))))

(defn- loop-avoiding-or-loop-erased-next-cell
  [cell path grid unvisited]
  (or (loop-avoiding-next-cell cell path grid unvisited)
      (loop-erased-next-cell cell path grid unvisited)))

(defn- loop-over-unvisited-cells 
  [next-cell-fn]
  (fn [grid unvisited]
    (loop [cell (rand-nth (seq unvisited))
           path [cell]]
      (if (contains? unvisited cell)
        (if-let [{:keys [next-cell updated-path]} (next-cell-fn cell path grid unvisited)]
          (recur next-cell updated-path)
          path)
        path))))

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
      (let [path ((loop-over-unvisited-cells loop-erased-next-cell) grid unvisited)
            grid (reduce link-cells grid (map (fn [a b] [a b]) (butlast path) (rest path)))
            unvisited (set/difference unvisited (set path))]
        (recur (rand-nth (seq unvisited)) unvisited grid))
      grid)))
