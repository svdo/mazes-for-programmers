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
