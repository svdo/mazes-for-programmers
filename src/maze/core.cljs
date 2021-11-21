(ns maze.core
  (:require [helix.core :as hx :refer [$]]
            [helix.dom :as d]
            [maze.lib :refer [defnc]]
            ["react-dom" :as rdom]))

(defnc App []
  (d/h1 "Maze"))

(defn ^:export start
  []
  (js/console.debug "START!!!!!!")
  (rdom/render ($ App) (js/document.getElementById "app")))
