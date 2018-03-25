(ns fsynth.core
  (:require
   [reagent.core :as reagent]
   [fsynth.audio :as audio]
   [fsynth.state :as state]
   ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Styles 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def page-style
  {:style
   {:height           "800px"
    :width            "100%"
    :padding          "100px"
    :background-color "black"}})

(def seq-container-style 
  {:style
   {:box-sizing "border-box"
    :width      "600px"
    :height     "600px"
    :border     "3px solid red"}})
  
(def bpm-selector-style
  {:width "40px"
   :text-align "center"})

(def scale-selector-style
  {:text-align "center"})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Views 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn scale-selector [state]
  [:select {:multiple false 
            :style scale-selector-style
            :value (name (:mode @state))
            :on-change #(swap! state assoc :mode (keyword (-> % .-target .-value)))}
   [:option {:value :ionian}     "major"]
   [:option {:value :aeolian}    "minor"]
   [:option {:value :dorian}     "dorian"]
   [:option {:value :phrygian}   "phrygian"]
   [:option {:value :lydian}     "lydian"]
   [:option {:value :mixolydian} "mixolydian"]
   ])

(defn sequencer-grid []
  [:div seq-container-style "Howdy!!"])

(defn bpm-selector [state]
  [:input {:type "text"
           :style bpm-selector-style
           :value (:tempo @state)
           :on-input #(swap! state assoc :tempo (-> % .-target .-value))}])

(defn play-button [song bpm]
  [:button {:onClick #(audio/play-sequence song bpm)} "PLAY"])

(audio/play-sequence [{:frequency 400 :octace 1 :duration 1}] 200)

(defn page [state]
  (prn "STATE" @state)
  [:div page-style
   [:div {:color "#fff"} (str @state)]
   (sequencer-grid)
   (play-button (audio/song) (:tempo @state))
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
