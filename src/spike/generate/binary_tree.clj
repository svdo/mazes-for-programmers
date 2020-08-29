(ns spike.generate.binary-tree
  (:require [spike.grid :as grid]))

(defn- carve-cell
  [grid cell]
  (let [neighbors (remove nil? ((juxt :north :east) cell))]
    (when (seq neighbors)
      [[(:row cell) (:col cell)] (rand-nth neighbors)])))

(defn- carve-row
  [grid row]
  (map (partial carve-cell grid) row))

(defn- apply-carvings
  [grid carvings]
  (letfn [(apply-carving [g [from to]] (grid/link-cells g from to))]
    (reduce apply-carving grid carvings)))

(defn carve
  [grid]
  (let [carvings (remove nil? (mapcat (partial carve-row grid) grid))]
    (apply-carvings grid carvings)))
