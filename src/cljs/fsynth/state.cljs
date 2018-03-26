(ns fsynth.state
  (:require
   [reagent.core :as reagent]))

(def note-rows
  {
   :1 [0 1 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0]
   :2 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :3 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :4 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :5 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :6 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :j [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :8 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   })

(def state
  (reagent/atom
   {:mode :lydian
    :tempo 120
    :notes note-rows}))
