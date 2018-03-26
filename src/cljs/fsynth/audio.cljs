(ns fsynth.audio
  (:require
   [reagent.core :as reagent]
   [fsynth.state :as state]
   ))

(def modes
  {:ionian     {:1 0 :2 2 :3 4 :4 5 :5 7 :6 9 :7 11 :8 12}
   :dorian     {:1 0 :2 2 :3 3 :4 5 :5 7 :6 9 :7 11 :8 12}
   :phrygian   {:1 0 :2 1 :3 3 :4 5 :5 7 :6 8 :7 10 :8 12}
   :lydian     {:1 0 :2 2 :3 4 :4 6 :5 7 :6 9 :7 11 :8 12}
   :mixolydian {:1 0 :2 2 :3 3 :4 5 :5 7 :6 8 :7 11 :8 12}
   :aeolian    {:1 0 :2 2 :3 3 :4 5 :5 7 :6 8 :7 10 :8 12}})

(defn hs
  "Determines how many half-steps in each number by mode"
  [n]
  (let [index (-> n str keyword)
        mode  (:mode @state/state)]
    (get-in modes [mode index])))

(defn hz 
  "Determines frequency in hz from half-steps above given frequency"
  [n]
  (if (= 0 n)
    0
    (* 110 (Math/pow 1.059463 (hs n)))))

(defn dur
  "Note duration based on box number"
  [box]
  (case box
    1 0.125
    2 0.25
    3 0.25
    4 0.5
    5 0.5 
    6 1
    7 1
    8 2
    9 2))

(defn build-note
  "Prepares note map to be sent to oscillators"
  ([freq octave duration]
  {:frequency freq 
   :octave    octave
   :duration  duration})
  ([freq octave]
   (build-note freq octave 1))
  ([freq]
   (build-note freq 1 0.25)))

(defn song []
  (mapv #(build-note (hz %) 1 0.25)
        [0 0 0 0 0 0 3 5 3 6 5 0 0 0  0 5 0 6 0 7 0 1 0 2 0 4 0 6 0 3 0 2]
        #_(concat (range 0 8) (reverse (range 0 7)))))

(defn song2 []
  (mapv #(build-note (hz %) 3 0.25)
        [3 5 3 6 5 0 0 0 0 0 0 0 5 0 6 0 7 0 1 0 2 0 4 0 6 0 3 0 2]))

(def ctx 
  "Initial music context constructor"
  (let [constructor (or js/window.AudioContext
                        js/window.webkitAudioContext)]
    (constructor.)))

(defn play-note
  "Creates a synthesizer that connects web audio parts and generates frequency"
  [freq octave sustain]
  (let [osc     (.createOscillator ctx)
        vol     (.createGain ctx)
        wave    "square"]
    (.connect osc vol)
    (.connect vol (.-destination ctx))
    
    (set! (.-valoe (.-gain vol)) 0)
    (.setTargetAtTime (.-gain vol) 0.25 (.-currentTime ctx) 0.01)
    (.setTargetAtTime (.-gain vol) 0.00 (+ (.-currentTime ctx) sustain) 0.05)

    (set! (.-value  (.-frequency osc)) (* freq octave))
    (set! (.-type osc) wave)

    (.start osc (.-currentTime ctx))
    (.stop osc (+ (.-currentTime ctx) sustain 0.1))))

(defn play-sequence
  "Schedules a sequence of notes to be sent to play-note"
  [notes bpm]
  (prn "#$#$#$" notes)
  (let [xs       (first notes)
        ys       (rest notes)
        freq     (:frequency xs)
        octave   (:octave xs)
        duration (:duration xs)
        sustain  (* (/ 60 bpm) duration)]
    (prn "HI" freq octave sustain)
    (play-note freq octave sustain)
    (when-not (empty? ys)
      (js/setTimeout #(play-sequence ys bpm) (* 1000 sustain)))))

(defn play-all-notes [notes bpm]
  (play-sequence (mapv #(build-note (hz %) 4) (:4 notes)) bpm)
  (play-sequence (mapv #(build-note (hz %) 3) (:3 notes)) bpm)
  (play-sequence (mapv #(build-note (hz %) 2) (:2 notes)) bpm)
  (play-sequence (mapv #(build-note (hz %) 1) (:1 notes)) bpm)
  )


