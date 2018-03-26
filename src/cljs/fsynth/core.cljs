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
    :width            "100%"
    :margin           "0 auto"
    :padding          "100px"
    :background-color "black"}})

(defn note-style [on?]
   {:height           "20px"
    :width            "20px"
    :margin           "10px"
    :background-color (if-not on? "#111" "red")})

(def seq-container-style 
  {:style
   {:box-sizing       "border-box"
    ;; :display          "flex"
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

(def scale-selector-style
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

(defn sequencer-grid [state]
  [:div seq-container-style
   [:div note-container-style (map #(note 16 % state) (range 1 17))]
   [:div note-container-style (map #(note 15 % state) (range 1 17))]
   [:div note-container-style (map #(note 14 % state) (range 1 17))]
   [:div note-container-style (map #(note 13 % state) (range 1 17))]
   [:div note-container-style (map #(note 12 % state) (range 1 17))]
   [:div note-container-style (map #(note 11 % state) (range 1 17))]
   [:div note-container-style (map #(note 10 % state) (range 1 17))]
   [:div note-container-style (map #(note 9 % state) (range 1 17))]
   [:div note-container-style (map #(note 8 % state) (range 1 17))]
   [:div note-container-style (map #(note 7 % state) (range 1 17))]
   [:div note-container-style (map #(note 6 % state) (range 1 17))]
   [:div note-container-style (map #(note 5 % state) (range 1 17))]
   [:div note-container-style (map #(note 4 % state) (range 1 17))]
   [:div note-container-style (map #(note 3 % state) (range 1 17))]
   [:div note-container-style (map #(note 2 % state) (range 1 17))]
   [:div note-container-style (map #(note 1 % state) (range 1 17))]
   ])

(defn bpm-selector [state]
  [:input {:type "text"
           :style bpm-selector-style
           :value (:tempo @state)
           :on-input #(update/update-tempo (-> % .-target .-value))}])

(defn play-button [state]
  [:button {:onClick #(audio/play-all-notes (:notes @state) (:tempo @state))} "PLAY"])

(defn page [state]
  [:div page-style
   (sequencer-grid state)
   (play-button state)
   (scale-selector state)
   (bpm-selector state)])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (reagent/render [page state/state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
