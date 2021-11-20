(ns spike.solve.dijkstra
  (:require [clojure.set :as set]
            [spike.grid :as grid]))

(defn has-distance? [cell]
  (some? (:distance cell)))

(defn set-cell-distance [distance grid cell]
  ;; (println "set-cell-distance " distance " " (grid/coords cell))
  (cond-> grid
    (not (has-distance? cell))
    (assoc-in [(:row cell) (:col cell) :distance] distance)))

(defn same-coord? [cell1 cell2]
  (= (grid/coords cell1)
     (grid/coords cell2)))

(defn assign-distances-to-linked-cells [next-distance [grid new-frontier] cell]
  ;; (println "---------- [" next-distance "]")
  ;; (print (spike.render.ascii/to-str grid (comp str :distance)) )
  ;; (println "cur cell:" (grid/coords cell))
  (let [linked (grid/linked-cells grid (:row cell) (:col cell))
        next-frontier (-> new-frontier
                          (set/union (set (filter (complement has-distance?) linked)))
                          ((partial remove (partial same-coord? cell)))
                          set)]
    ;; (println "next frontier: " (map grid/coords next-frontier))
    [(reduce (partial set-cell-distance next-distance) grid linked)
     next-frontier]))

(defn assign-distances-to-frontier [grid frontier next-distance]
  (reduce (partial assign-distances-to-linked-cells next-distance) [grid #{}] frontier))

(defn assign-distances [grid row col]
  (loop [grid     (assoc-in grid [row col :distance] 0)
         frontier #{(grid/get-cell grid row col)}
         distance 1]
    (if (empty? frontier)
      grid
      (let [[updated-grid new-frontier] (assign-distances-to-frontier grid frontier distance)]
        (recur  updated-grid new-frontier (inc distance))))))
