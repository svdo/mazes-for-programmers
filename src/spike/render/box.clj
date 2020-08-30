(ns spike.render.box
  (:require [spike.grid :as grid]
            [clojure.string :as s]))

(defn- cell->ascii->middle
  [cell]
  (s/join ["   "
           (if ((:links cell) (:east cell))
             " "
             "│")]))

(defn- cell->ascii->bottom
  [row next-row]
  (letfn [(cell->ascii->bottom'
            [[cell & rest :as cells] [next-row-cell & next-row-rest] prev-cell parts]
            (if (empty? cells)
              parts
              (let [left-side (cond
                                (and (empty? parts) (nil? (:south cell)))
                                "└"

                                (empty? parts)
                                (if (some? ((:links cell) (:south cell))) "│" "├")

                                (nil? (:south cell))
                                (if (some? ((:links cell) (:west cell))) "─" "┴")

                                (and (some? next-row-cell)
                                     (some? ((:links next-row-cell) (:west next-row-cell))))
                                (cond (some? ((:links cell) (:west cell))) "─"
                                      (some? ((:links prev-cell) (:south prev-cell))) "└"
                                      (some? ((:links cell) (:south cell))) "┘"
                                      :else "┴")

                                (some? ((:links cell) (:south cell)))
                                (cond (some? ((:links prev-cell) (:south prev-cell))) "│"
                                      (some? ((:links cell) (:west cell))) "┐"
                                      :else "┤")

                                (some? ((:links cell) (:west cell)))
                                (if (some? ((:links prev-cell) (:south prev-cell))) "┌" "┬")

                                (and (some? prev-cell)
                                     (some? ((:links prev-cell) (:south prev-cell))))
                                "├"

                                :else
                                "┼")
                    mid-part (if (some? ((:links cell) (:south cell))) "   " "───")
                    right-side (or (when (empty? rest)
                                     (cond (nil? (:south cell)) "┘"
                                           (some? ((:links cell) (:south cell))) "│"
                                           :else "┤"))
                                   "")]
                (recur rest next-row-rest cell (conj parts (str left-side mid-part right-side))))))]
    (cell->ascii->bottom' row next-row nil [])))

(defn- row->ascii
  [row next-row]
  (str "│"
       (s/join (map cell->ascii->middle row))
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
  [grid]
  (str "\n" (top-border (first grid)) "\n"
       (s/join (map row->ascii
                    grid
                    (conj (vec (rest grid)) nil)))))
