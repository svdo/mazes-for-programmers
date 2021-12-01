(ns maze.render.box
  (:require [clojure.string :as s]
            [maze.grid :refer [linked?]]
            [maze.string-utils :refer [center-string]]))

(defn- cell->ascii->middle
  [content-fn cell]
  (s/join [(center-string 3 (or (content-fn cell) ""))
           (if (linked? cell :east)
             " "
             "│")]))

(defn- cell->ascii->bottom
  [row next-row]
  (letfn [(cell->ascii->bottom'
            [[cell & rest :as cells] [next-row-cell & next-row-rest] prev-cell parts]
            (if (empty? cells)
              parts
              (let [left-side (cond
                                (empty? parts) ;; first cell of the row
                                (cond
                                  (nil? (:south cell)) "└"
                                  (linked? cell :south) "│"
                                  :else "├")

                                (nil? (:south cell))
                                (if (linked? cell :west) "─" "┴")

                                (linked? next-row-cell :west)
                                (cond (linked? cell :west) "─"
                                      (linked? prev-cell :south) "└"
                                      (linked? cell :south) "┘"
                                      :else "┴")

                                (linked? cell :south)
                                (cond (linked? prev-cell :south) "│"
                                      (linked? cell :west) "┐"
                                      :else "┤")

                                (linked? cell :west)
                                (if (linked? prev-cell :south) "┌" "┬")

                                (linked? prev-cell :south)
                                "├"

                                :else
                                "┼")
                    mid-part (if (linked? cell :south) "   " "───")
                    right-side (or (when (empty? rest)
                                     (cond (nil? (:south cell)) "┘"
                                           (linked? cell :south) "│"
                                           :else "┤"))
                                   "")]
                (recur rest next-row-rest cell (conj parts (str left-side mid-part right-side))))))]
    (cell->ascii->bottom' row next-row nil [])))

(defn- row->ascii
  [content-fn row next-row]
  (str "│"
       (s/join (map (partial cell->ascii->middle content-fn) row))
       "\n"
       (s/join (cell->ascii->bottom row next-row))
       "\n"))

(defn- top-border
  [top-row]
  (letfn [(top-border'
            [[cell & rest :as cells] parts]
            (if (empty? cells)
              parts
              (let [right-side (cond
                                 (empty? rest)
                                 "┐"
                                 (some? ((:links cell) (:east cell)))
                                 "─"
                                 :else
                                 "┬")]
                (recur rest (conj parts (str "───" right-side))))))]
    (str "┌" (s/join (top-border' top-row [])))))

(defn to-str
  ([grid]
   (to-str grid (constantly " ")))
  ([grid content-fn]
   (str "\n" (top-border (first grid)) "\n"
        (s/join (map (partial row->ascii content-fn)
                     grid
                     (conj (vec (rest grid)) nil))))))
