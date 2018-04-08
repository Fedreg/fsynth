(ns fsynth.core
  (:require
   [reagent.core  :as reagent]
   [fsynth.audio  :as audio]
   [fsynth.state  :as state]
   [fsynth.update :as update]
   ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Styles 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def page-style
  {:style
   {:height           "800px"
    :width            "600px"
    :margin           "0 auto"
    :padding          "100px"
    :background-color "black"}})

(defn note-style [on?]
   {:height           "20px"
    :width            "20px"
    :margin           "10px"
    :border-radius    "10px"
    :background-color (if-not on? "#111" "red")})

(def seq-container-style
  {:style
   {:box-sizing       "border-box"
    :width            "600px"
    :height           "650px"
    :background-color "#000"
    :border           "3px solid #111"}})

(def note-container-style
  {:style
   {:display          "flex"}})

(def bpm-selector-style
  {:width "40px"
   :text-align "center"})

(def play-button-style
  {:width "0"
   :height "0"
   :font-size "0"
   :line-height "0%"
   :margin "20px"
   :border-top "15px solid transparent"
   :border-left "30px solid #333"
   :border-bottom "15px solid transparent"})

(def stop-button-style
  {:width "30px"
   :height "30px"
   :margin "20px"
   :background-color "#333"})

(def scale-selector-style
  {:text-align "center"})

(def wave-selector-style
  {:text-align "center"})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Views
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn note [row n state]
  (let [index (keyword (str row))
        notes (-> @state :notes index)
        on? (when (= 1 (nth notes n)) true)]
    [:div {:style (note-style on?)
           :id (str row "." n)
           :onClick #(update/update-notes (-> % .-target .-id))}]))

(defn scale-selector [state]
  [:select {:multiple false
            :style scale-selector-style
            :value (name (:mode @state))
            :on-change #(update/update-mode (-> % .-target .-value))}
   [:option {:value :ionian}     "major"]
   [:option {:value :aeolian}    "minor"]
   [:option {:value :dorian}     "dorian"]
   [:option {:value :phrygian}   "phrygian"]
   [:option {:value :lydian}     "lydian"]
   [:option {:value :mixolydian} "mixolydian"]
   ])

(defn wave-selector [state]
  [:select {:multiple false 
            :style wave-selector-style
            :value (name (:wave @state))
            :on-change #(update/update-wave (-> % .-target .-value))}
   [:option {:value "sine"}     "sine"]
   [:option {:value "triangle"} "triangle"]
   [:option {:value "square"}   "square"]
   [:option {:value "sawtooth"} "sawtooth"]
   ])

(defn sequencer-grid [state]
  [:div seq-container-style
   [:div note-container-style (map #(note 16 % state) (range 0 16))]
   [:div note-container-style (map #(note 15 % state) (range 0 16))]
   [:div note-container-style (map #(note 14 % state) (range 0 16))]
   [:div note-container-style (map #(note 13 % state) (range 0 16))]
   [:div note-container-style (map #(note 12 % state) (range 0 16))]
   [:div note-container-style (map #(note 11 % state) (range 0 16))]
   [:div note-container-style (map #(note 10 % state) (range 0 16))]
   [:div note-container-style (map #(note 9 % state) (range 0 16))]
   [:div note-container-style (map #(note 8 % state) (range 0 16))]
   [:div note-container-style (map #(note 7 % state) (range 0 16))]
   [:div note-container-style (map #(note 6 % state) (range 0 16))]
   [:div note-container-style (map #(note 5 % state) (range 0 16))]
   [:div note-container-style (map #(note 4 % state) (range 0 16))]
   [:div note-container-style (map #(note 3 % state) (range 0 16))]
   [:div note-container-style (map #(note 2 % state) (range 0 16))]
   [:div note-container-style (map #(note 1 % state) (range 0 16))]
   ])

(defn bpm-selector [state]
  [:input {:type "text"
           :style bpm-selector-style
           :value (:tempo @state)
           :on-input #(update/update-tempo (-> % .-target .-value))}])

(defn play-button [state]
  [:div {:style (if (:playing? @state) stop-button-style play-button-style)
         :onClick #(do (update/update-playing-state)
                       (audio/play-all-notes))}])

(defn clear-button []
  [:button {:onClick #(do (update/clear-all-notes)
                          (update/update-playing-state))} "CLEAR ALL"])

(defn page [state]
  [:div page-style
   [:div {:style {:color "white"}} (str @state/state)]
   (sequencer-grid state)
   (play-button state)
   (clear-button)
   (scale-selector state)
   (wave-selector state)
   (bpm-selector state)])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    ))

(defn reload []
  (reagent/render [page state/state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
