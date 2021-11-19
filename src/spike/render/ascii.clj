(ns spike.render.ascii
  (:require [spike.grid :as grid]
            [clojure.string :as str]))

(defn center-string [width s]
  (let [s (if (>= width (count s)) s (subs s 0 width))
        pad-size (- width (count s))
        pad-start (quot (+ 1 (count s) pad-size) 2)]
    (->> s
         (format (str "%" pad-start "s"))
         (format (str "%-" width "s")))))

(defn- cell->ascii->middle
  [content-fn cell]
  (str/join [(center-string 3 (or (content-fn cell) ""))
             (if ((:links cell) (:east cell))
               " "
               "|")]))

(defn- cell->ascii->bottom
  [cell]
  (str/join [(if ((:links cell) (:south cell))
               "   "
               "---")
             "+"]))

(defn- row->ascii
  [content-fn row]
  (str "|" (str/join (map (partial cell->ascii->middle content-fn) row)) "\n"
       "+" (str/join (map cell->ascii->bottom row)) "\n"))

(defn to-str
  ([grid]
   (to-str grid (constantly " ")))
  ([grid content-fn]
   (str "\n+" (str/join "+" (repeat (grid/width grid) "---")) "+\n"
        (apply str (map (partial row->ascii content-fn) grid)))))
