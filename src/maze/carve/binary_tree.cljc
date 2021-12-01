(ns maze.carve.binary-tree
  (:require [maze.grid :as grid]))

(defn- carve-cell
  [cell]
  (let [neighbors (remove nil? ((juxt :north :east) cell))]
    (when (seq neighbors)
      [[(:row cell) (:col cell)] (rand-nth neighbors)])))

(defn- carve-row
  [row]
  (map carve-cell row))

(defn- apply-carvings
  [grid carvings]
  (letfn [(apply-carving [g [from to]] (grid/link-cells g from to))]
    (reduce apply-carving grid carvings)))

(defn carve
  [grid]
  (let [carvings (remove nil? (mapcat carve-row grid))]
    (apply-carvings grid carvings)))
