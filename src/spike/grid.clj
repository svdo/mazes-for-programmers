(ns spike.grid)

(declare get-cell)

(defn- add-neighbors-for-cell
  [grid cell]
  (-> cell
      (assoc :north (get-cell grid (dec (:row cell)) (:col cell)))
      (assoc :south (get-cell grid (inc (:row cell)) (:col cell)))
      (assoc :west (get-cell grid (:row cell) (dec (:col cell))))
      (assoc :east (get-cell grid (:row cell) (inc (:col cell))))))

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

(defn get-cell
  [grid row col]
  (when (and (>= (dec (height grid)) row 0)
             (>= (dec (width grid)) col 0))
    (get-in grid [row col])))

(defn coords
  [cell]
  ((juxt :row :col) cell))

(defn link-cells
  [grid [row1 col1] [row2 col2]]
  (-> grid
      (update-in [row1 col1 :links]
                 #(conj % (get-cell grid row2 col2)))
      (update-in [row2 col2 :links]
                 #(conj % (get-cell grid row1 col1)))))
