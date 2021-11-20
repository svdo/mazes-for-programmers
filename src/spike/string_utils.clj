(ns spike.string-utils)

(defn center-string [width s]
  (let [s (if (>= width (count s)) s (subs s 0 width))
        pad-size (- width (count s))
        pad-start (quot (+ 1 (count s) pad-size) 2)]
    (->> s
         (format (str "%" pad-start "s"))
         (format (str "%-" width "s")))))
