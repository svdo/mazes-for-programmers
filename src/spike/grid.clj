(ns spike.grid)

(defn create
  [width height]
  (vec (for [row (range height)]
         (vec (for [col (range width)]
                {:row row :col col :links #{}})))))

(defn height
  [grid]
  (count grid))

(defn width
  [grid]
  (count (first grid)))

(defn cell
  [grid row col]
  (when (and (>= (dec (height grid)) row 0)
             (>= (dec (width grid)) col 0))
    (get-in grid [row col])))

(defn link-cells
  [grid [row1 col1] [row2 col2]]
  (-> grid
      (update-in [row1 col1 :links]
                 #(conj % (cell grid row2 col2)))
      (update-in [row2 col2 :links]
                 #(conj % (cell grid row1 col1)))))
