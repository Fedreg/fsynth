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

(def note-rows1
  {:14 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0]
   :12 [1 1 1 1 0 1 1 0 1 1 0 1 0 0 0 0]
   :11 [0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0]
   :10 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0]
   :13 [0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1]
   :4 [0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0]
   :16 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :7 [1 0 1 0 1 0 1 0 1 0 0 0 1 0 0 0]
   :1 [0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0]
   :8 [0 1 0 1 0 0 0 0 0 1 0 1 0 1 0 1]
   :9 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :2 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :5 [0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0]
   :15 [0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0]
   :3 [1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0]
   :6 [0 0 0 0 0 1 0 0 0 0 1 0 0 0 1 0]})

(def note-rows2
  {:14 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0]
   :12 [1 1 1 1 0 1 1 0 1 1 0 1 0 0 0 0]
   :11 [0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0]
   :10 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0]
   :13 [0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1]
   :4 [0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0]
   :16 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :7 [1 0 1 0 1 0 1 0 1 0 0 0 1 0 0 0]
   :1 [0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0]
   :8 [0 1 0 1 0 0 0 0 0 1 0 1 0 1 0 1]
   :9 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :2 [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
   :5 [0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0]
   :15 [0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0]
   :3 [1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0]
   :6 [0 0 0 0 0 1 0 0 0 0 1 0 0 0 1 0]})

(def state1
  (reagent/atom
   {:mode     :aeolian
    :tempo    300
    :wave     "square"
    :color    "red"
    :playing? false
    :index    0
    :notes    note-rows1}))

(def state2
  (reagent/atom
   {:mode     :aeolian
    :tempo    300
    :wave     "square"
    :color    "deeppink"
    :playing? false
    :index    0
    :notes    note-rows2}))
