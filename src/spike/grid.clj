(ns spike.grid)

(defn create
  [width height]
  (repeat height (repeat width [])))

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
    {}))
