(ns fsynth.state
  (:require
   [reagent.core :as reagent]))

(defn init-notes []
  (into [] (take 17 (repeatedly #(rand-int 2)))))

(def note-rows
  {
   :1 [1 0 0 0 1 0 0 0 1 0 0 0 1 0 0 0 0]
   :2 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :3 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :4 [0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0]
   :5 [1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0]
   :6 (init-notes)
   :7 (init-notes)
   :8 [1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :9 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :10 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :11 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :12 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :13 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :14 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :15 (init-notes)
   :16 (init-notes)
   })

(def state
  (reagent/atom
   {:mode :lydian
    :tempo 300 
    :notes note-rows}))
