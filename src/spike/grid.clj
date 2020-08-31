(ns spike.grid
  (:require [clojure.string :as s]))

(declare get-cell)
(declare valid-coords)

(defn- add-neighbors-for-cell
  [grid cell]
  (-> cell
      (assoc :north (valid-coords grid (dec (:row cell)) (:col cell)))
      (assoc :south (valid-coords grid (inc (:row cell)) (:col cell)))
      (assoc :west (valid-coords grid (:row cell) (dec (:col cell))))
      (assoc :east (valid-coords grid (:row cell) (inc (:col cell))))))

(defn- add-neighbors
  [grid]
  (mapv #(mapv (partial add-neighbors-for-cell grid) %) grid))

(defn create
  [width height]
  (let [cells (vec (for [row (range height)]
                     (vec (for [col (range width)]
                            {:row row :col col :links #{}}))))]
    (add-neighbors cells)))

(defn height
  [grid]
  (count grid))

(defn width
  [grid]
  (count (first grid)))

(defn- valid-coords?
  [grid row col]
  (and (>= (dec (height grid)) row 0)
       (>= (dec (width grid)) col 0)))

(defn- valid-coords
  [grid row col]
  (when (valid-coords? grid row col) [row col]))

(defn get-cell
  [grid row col]
  (when (valid-coords? grid row col)
    (get-in grid [row col])))

(defn coords
  [cell]
  ((juxt :row :col) cell))

(defn link-cells
  [grid [row1 col1] [row2 col2]]
  (-> grid
      (update-in [row1 col1 :links]
                 #(conj % [row2 col2]))
      (update-in [row2 col2 :links]
                 #(conj % [row1 col1]))))

(defn linked?
  [cell direction]
  (and (some? cell)
       (some? ((:links cell) (direction cell)))))

(defn map-cells
  [f grid]
  (mapv #(mapv f %) grid))
