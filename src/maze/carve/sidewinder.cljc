(ns maze.carve.sidewinder
  (:require [spike.grid :as grid]))

(defn- coin-flip
  []
  (= 1 (rand-int 2)))

(defn- carve-row'
  [[cell & remaining :as cells] run carvings]
  (if (empty? cells)
    carvings
    (let [should-close-run (or (nil? (:east cell))
                               (and (some? (:north cell)) (coin-flip)))
          new-run (conj run [(:row cell) (:col cell)])]
      (if should-close-run
        (let [lucky-cell (rand-nth new-run)
              new-carvings (if (> (nth lucky-cell 0) 0)
                             (conj carvings [lucky-cell (update lucky-cell 0 dec)])
                             carvings)]
          (recur remaining
                 []
                 new-carvings))
        (let [this-cell (grid/coords cell)]
          (recur remaining
                 new-run
                 (conj carvings [this-cell (update this-cell 1 inc)])))))))

(defn- carve-row
  [row]
  (carve-row' row [] []))

(defn- apply-carvings
  [grid carvings]
  (letfn [(apply-carving [g [from to]] (grid/link-cells g from to))]
    (reduce apply-carving grid carvings)))

(defn carve
  [grid]
  (let [carvings (remove nil? (mapcat carve-row grid))]
    (apply-carvings grid carvings)))
