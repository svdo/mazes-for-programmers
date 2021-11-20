(ns spike.solve.dijkstra
  (:require [clojure.set :as set]
            [spike.grid :as grid]
            [spike.render.ascii]))

(defn has-distance? [cell]
  (some? (:dijkstra/distance cell)))

(defn set-cell-distance [distance grid cell]
  ;; (println "set-cell-distance " distance " " (grid/coords cell))
  (cond-> grid
    (not (has-distance? cell))
    (assoc-in [(:row cell) (:col cell) :dijkstra/distance] distance)))

(defn assign-distances-to-linked-cells [next-distance [grid new-frontier] cell]
  ;; (println "---------- [" next-distance "]")
  ;; (print (spike.render.ascii/to-str grid (comp str :dijkstra/distance)) )
  ;; (println "cur cell:" (grid/coords cell))
  (let [linked (grid/linked-cells grid (:row cell) (:col cell))
        next-frontier (-> new-frontier
                          (set/union (set (filter (complement has-distance?) linked)))
                          ((partial remove (partial grid/same-coord? cell)))
                          set)]
    ;; (println "next frontier: " (map grid/coords next-frontier))
    [(reduce (partial set-cell-distance next-distance) grid linked)
     next-frontier]))

(defn assign-distances-to-frontier [grid frontier next-distance]
  (reduce (partial assign-distances-to-linked-cells next-distance) [grid #{}] frontier))

(defn assign-distances [grid row col]
  (loop [grid     (assoc-in grid [row col :dijkstra/distance] 0)
         frontier #{(grid/get-cell grid row col)}
         distance 1]
    (if (empty? frontier)
      grid
      (let [[updated-grid new-frontier] (assign-distances-to-frontier grid frontier distance)]
        (recur updated-grid new-frontier (inc distance))))))

(defn neighbor-with-lowest-distance [grid [row col] max-distance]
  ;; (print (spike.render.ascii/to-str grid (comp str :dijkstra/distance)) )
  ;; (println "Find for " [row col] " lower than " max-distance)
  (->> (grid/linked-cells grid row col)
      ;;  (#(do (println "  " %) %))
       (filter #(< (:dijkstra/distance %) max-distance))
       (sort-by :dijkstra/distance)
       first
       grid/coords))

(defn mark-shortest-path [grid
                          from-coord
                          [to-row to-col :as to-coord]]
  (loop [curr to-coord
         grid (assoc-in grid [to-row to-col :dijkstra/on-shortest-path] true)]
    (if (= curr from-coord)
      grid
      (let [[row col] (neighbor-with-lowest-distance grid 
                                                     curr
                                                     (:dijkstra/distance (grid/get-cell grid curr)))]
        (recur [row col] (assoc-in grid [row col :dijkstra/on-shortest-path] true))))))
