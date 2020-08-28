(ns spike.maze
  (:gen-class)
  (:require [lanterna.screen :as s]
            [lanterna.terminal :as t]))

(def term (t/get-terminal :unix))

(defn -main
  [& _]
  ;; (t/start term)
  ;; (t/put-character term \H)
  ;; (t/move-cursor term 40 12)
  ;; (t/put-character term \@)
  )
