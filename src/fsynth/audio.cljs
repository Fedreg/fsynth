(ns fsynth.audio
  (:require
   [reagent.core  :as reagent]
   [fsynth.state  :as state]
   [fsynth.update :as update]
   ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Web Audio Constructor
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ctx
  "Initial music context constructor"
  (let [constructor (or js/window.AudioContext
                        js/window.webkitAudioContext)]
    (constructor.)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Build Notes
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def modes
  {:ionian     {:1 0 :2 2 :3 4 :4 5 :5 7 :6 9 :7 11 :8 12}
   :dorian     {:1 0 :2 2 :3 3 :4 5 :5 7 :6 9 :7 11 :8 12}
   :phrygian   {:1 0 :2 1 :3 3 :4 5 :5 7 :6 8 :7 10 :8 12}
   :lydian     {:1 0 :2 2 :3 4 :4 6 :5 7 :6 9 :7 11 :8 12}
   :mixolydian {:1 0 :2 2 :3 3 :4 5 :5 7 :6 8 :7 11 :8 12}
   :aeolian    {:1 0 :2 2 :3 3 :4 5 :5 7 :6 8 :7 10 :8 12}})

(defn hs
  "Determines how many half-steps in each number by mode"
  [state n]
  (let [index (-> n str keyword)
        mode  (:mode @state/global-state)]
    (get-in modes [mode index])))

(defn hz
  "Determines frequency in hz from half-steps above given frequency"
  [state n]
  (if (= 0 n)
    0
    (* 110 (Math/pow 1.059463 (hs state n)))))

(defn build-note
  "Prepares note map to be sent to oscillators"
  ([freq octave duration]
  {:frequency freq
   :octave    octave
   :duration  duration})
  ([freq octave]
   (build-note freq octave 1))
  ([freq]
   (build-note freq 1 0.125)))

(defn modify-notes
  "Adds the key number to the notes to be played to increase half steps"
  [notes hs key]
  (let [coll (key notes)
        new  (map (fn [n] (if (= 0 n) n (+ n hs))) coll)]
    new))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Play Notes
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn play-note
  "Creates a synthesizer that connects web audio parts and generates frequency"
  [state freq octave sustain]
  (let [osc     (.createOscillator ctx)
        vol     (.createGain ctx)
        gain    (.-gain vol)
        wave    (or (:wave @state) (:wave @state/global-state))
        time    (.-currentTime ctx)]
    (.connect osc vol)
    (.connect vol (.-destination ctx))

    (set! (.-valoe gain) 0)
    (.setTargetAtTime gain 0.50 time 0.01)
    (.setTargetAtTime gain 0.00 (+ time sustain) 0.05)

    (set! (.-value  (.-frequency osc)) (* freq octave))
    (set! (.-type osc) wave)

    ;; (prn "time" (.-currentTime ctx))
    (.start osc time)
    (.stop osc (+ time sustain 0.1))))

(defn play-sequence
  "Schedules a sequence of notes to be sent to play-note"
  [state notes & [index]]
  (let [xs       (first notes)
        ys       (rest notes)
        freq     (:frequency xs)
        octave   (:octave    xs)
        duration (:duration  xs)
        bpm      (:tempo @state/global-state)
        sustain  (* (/ 60 bpm) duration)]
    ;; (prn 'STUFF xs ys sustain)
    (play-note state freq octave sustain)
    (when-not (empty? ys)
      (js/setTimeout
       #(if index
          (do
            (play-sequence state ys (inc index))
            (update/update-index (inc index)))
          (play-sequence state ys))
       (* 1000 sustain)))))

(defn play-all-notes [state]
  "Play all the notes at once"
  (let [notes    (:notes @state)
        bpm      (:tempo @state/global-state)
        playing? (:playing? @state)
        repeat   (* (/ 60 bpm) 1000 16)]
    (update/update-index 1)
    (when playing?
      (play-sequence state (mapv #(build-note (hz state %) 4) (modify-notes notes 1 :16)) 1)
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 7 :15)))
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 6 :14)))
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 5 :13)))
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 4 :12)))
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 3 :11)))
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 2 :10)))
      (play-sequence state (mapv #(build-note (hz state %) 2) (modify-notes notes 1 :9)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 7 :8)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 6 :7)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 5 :6)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 4 :5)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 3 :4)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 2 :3)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 1 :2)))
      (play-sequence state (mapv #(build-note (hz state %) 1) (modify-notes notes 0 :1))))
    (when playing?
      (js/setTimeout #(play-all-notes state) repeat))))

(defn master-play []
  "Master play button toggles play on all sequencers simultaneously"
  (mapv
   (fn [s]
     (update/update-playing-state s)
     (play-all-notes s)) 
   (:all-states @state/global-state)))

(defn master-stop []
  "Master stop button toggles stop on all sequencers"
  (mapv #(update/update-playing-state %) (:all-states @state/global-state)))
