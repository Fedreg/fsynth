(ns fsynth.update
  (:require
   [fsynth.state   :as state]
   [clojure.string :as str]))

(defn update-notes [note]
  "activates or deactivates note in sequencer"
  (let [cur    (str/split note ".")
        row    (keyword (first cur))
        n      (js/parseInt (last cur))
        cur    (get-in @state/state [:notes row n])]
    (swap! state/state update-in [:notes row n] (if (= 0 cur) inc dec))))

(defn update-mode [mode]
  "Changes the state to the selected mode"
  (swap! state/state assoc :mode (keyword mode)))

(defn update-tempo [bpm]
  "Changes the state to selected bpm"
  (swap! state/state assoc :tempo bpm))
