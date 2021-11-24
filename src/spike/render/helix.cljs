(ns spike.render.helix
  (:require [spike.grid :as grid]
            [helix.core :refer [$ <>]]
            [helix.dom :as d]
            [maze.lib :refer [defnc]]
            [clojure.string :as str]))

  ;; grid-template-rows: 4px 1fr 4px 1fr 4px 1fr 4px;
  ;; grid-template-columns: 4px 1fr 4px 1fr 4px 1fr 4px;

(def border-width 4)

(defn- grid-template-spec [num-cells]
  (let [border (str border-width "px ")
        element "1fr "]
    (apply str border (repeat num-cells (str element border)))))

(defnc BorderPart [{:keys [class-name]}]
  (d/div {:class-name class-name}))

(defnc h-border [{:keys [grid w h row]}]
  (let [class-name-base (cond-> "border horizontal"
                          (= row -1)
                          (str " first")

                          (= row (dec h))
                          (str " last"))]
    (<> (cons ($ BorderPart {:class-name class-name-base
                             :key (str "b-" (* 2 row) "-" -1)})
              (mapcat (fn [col]
                        [(let [cell (grid/get-cell grid row col)
                               class-name (cond-> class-name-base
                                            (= w col)
                                            (str " last")

                                            (grid/linked? cell :south)
                                            (str " open"))]
                           ($ BorderPart {:class-name class-name
                                          :key (str "b-" (* 2 row) "-" (* 2 col))}))
                         ($ BorderPart {:class-name class-name-base
                                        :key (str "b-" (* 2 row) "-" (inc (* 2 col)))})])
                      (range 0 w))))))

(defnc cells-row [{:keys [grid content-fn w row]}]
  (<> (cons (d/div {:class-name "border vertical first"
                    :key (str "b-" (inc (* 2 row)) "-" -1)})
            (mapcat (fn [col]
                      (let [cell (grid/get-cell grid row col)]
                        [(d/div {:class-name "cell"
                                 :key (str "c-" (inc (* 2 row)) "-" (* 2 col))}
                                (content-fn cell))
                         (let [class-name (cond-> "border vertical"
                                            (= (dec w) col)
                                            (str " last")

                                            (grid/linked? cell :east)
                                            (str " open"))]
                           (d/div {:class-name class-name
                                   :key (str "b-" (inc (* 2 row)) "-" (inc (* 2 col)))}))]))
                    (range 0 w)))))

(defnc Grid [{grid :grid
              content-fn :content-fn :or {content-fn (constantly " ")}}]
  (let [w (grid/width grid)
        h (grid/height grid)]
    (d/div
     {:class-name "grid"
      :style {:grid-template-rows (grid-template-spec h)
              :grid-template-columns (grid-template-spec w)
              :width (* 50 w)
              :height (* 50 h)}}
     ($ h-border {:grid grid :w w :h h :row -1})
     (<> (mapcat (fn [row]
                   [($ cells-row {:key (str "row-" row) :grid grid :content-fn content-fn :w w :row row})
                    ($ h-border {:key (str "h-border-" row) :grid grid :w w :h h :row row})])
                 (range 0 h))))))
