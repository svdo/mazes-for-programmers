(ns spike.render.ascii
  (:require [spike.grid :as grid]
            [clojure.string :as s]))

(defn- cell->ascii->middle
  [cell]
  (s/join [(format "%3s" (or (:contents cell) ""))
           (if ((:links cell) (:east cell))
             " "
             "|")]))

(defn- cell->ascii->bottom
  [cell]
  (s/join [(if ((:links cell) (:south cell))
             "   "
             "---")
           "+"]))

(defn- row->ascii
  [row]
  (str "|" (s/join (map cell->ascii->middle row)) "\n"
       "+" (s/join (map cell->ascii->bottom row)) "\n"))

(defn to-str
  [grid]
  (str "\n+" (s/join "+" (repeat (grid/width grid) "---")) "+\n"
       (apply str (map row->ascii grid))))
