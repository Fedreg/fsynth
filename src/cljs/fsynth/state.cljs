(ns fsynth.state
  (:require
   [reagent.core :as reagent]))

(defn init-notes []
  (into [] (take 17 (repeatedly #(rand-int 2)))))

(defn blank-notes []
  "Sets all notes to 0"
  (let [keys (map (fn [n] (keyword (str n))) (range 1 17))
        vals (into [] (take 16 (repeatedly #(rand-int 0))))]
    (zipmap keys (repeat 16 vals))))

(def note-rows
  {
   :1 [1 0 0 0 1 0 0 0 1 0 0 0 1 0 0 0 0]
   :2 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :3 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :4 [0 0 0 0 1 1 0 0 0 0 0 0 1 0 0 0 0]
   :5 [1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0]
   :6 [0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0]
   :7 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :8 [1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0]
   :9 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :10 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :11 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :12 [0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :13 [0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0]
   :14 [0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0]
   :15 [0 0 0 0 0 0 1 1 0 1 0 0 0 0 1 0 0]
   :16 [0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0]
   })

(def state
  (reagent/atom
   {:mode     :aeolian
    :tempo    300
    :wave     "square"
    :playing? false
    :index    0
    :notes    note-rows}))
