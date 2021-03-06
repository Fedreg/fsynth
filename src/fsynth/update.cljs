(ns fsynth.update
  (:require
   [fsynth.state   :as state]
   [clojure.string :as str]))

(defn update-notes [state note]
  "activates or deactivates note in sequencer"
  (let [cur    (str/split note ".")
        row    (keyword (first cur))
        n      (js/parseInt (last cur))
        cur    (get-in @state [:notes row n])]
    (swap! state update-in [:notes row n] (if (= 0 cur) inc dec))))

(defn update-mode [mode]
  "Changes the state to the selected mode"
  (swap! state/global-state assoc :mode (keyword mode)))

(defn update-wave [state wave]
  "Changes the state to the selected sound wave"
  (swap! state assoc :wave wave))

(defn update-tempo [bpm]
  "Changes the state to selected bpm"
  (swap! state/global-state assoc :tempo bpm))

(defn update-playing-state [state]
  "Changes the state to playing or not"
  (swap! state assoc :playing? (if (true? (:playing? @state)) false true)))

(defn clear-all-notes [state]
  "Clears out all the toggled notes in the sequencer"
  (swap! state assoc :notes (state/blank-notes))
  (swap! state assoc :playing? false))

(defn update-index [index]
  "Increases the index to control lighting effect"
  (swap! state/global-state assoc :index index))

(defn enlarge [state]
  "Controls if the sequencer window is enlarged or not"
  (swap! state assoc :zoom? (if (true? (:zoom? @state)) false true)))
