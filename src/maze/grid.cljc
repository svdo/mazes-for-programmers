(ns maze.grid)

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
  ([width height]
   (create width height nil nil))
  ([width height content-key content-fn]
   (let [cells (vec (for [row (range height)]
                      (vec (for [col (range width)]
                             (cond-> {:row row :col col :links #{}}
                               (some? content-key)
                               (assoc content-key (content-fn row col)))))))]
     (add-neighbors cells))))

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
  ([grid [row col]]
   (get-cell grid row col))
  ([grid row col]
   (when (valid-coords? grid row col)
     (get-in grid [row col]))))

(defn coords
  [cell]
  ((juxt :row :col) cell))

(defn same-coord? [cell1 cell2]
  (= (coords cell1)
     (coords cell2)))

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

(defn linked-cells [grid row col]
  (set (map (partial apply get-cell grid)
            (:links (get-cell grid row col)))))

(defn map-cells
  [f grid]
  (mapv #(mapv f %) grid))
