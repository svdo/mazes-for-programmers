(ns maze.use-interval 
  (:require [helix.hooks :as hooks]))

;; use-interval hook as suggested by Dan Abramov himself:
;; https://overreacted.io/making-setinterval-declarative-with-react-hooks/

(defn use-interval [callback delay]
  (let [saved-callback (hooks/use-ref nil)]
    
    (hooks/use-effect
     [callback]
     (reset! saved-callback callback))
    
    (hooks/use-effect
     [delay]
     (letfn [(tick [] (@saved-callback))]
       (when delay 
         (let [id (js/setInterval tick delay)]
           #(js/clearInterval id)))))))
