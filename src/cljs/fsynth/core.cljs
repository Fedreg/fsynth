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

(defn note-style [on?]
  {:style
   {:height           "20px"
    :width            "20px"
    :margin           "10px"
    :background-color (if-not on? "#111" "#333")}})

(def seq-container-style 
  {:style
   {:box-sizing       "border-box"
    :display          "flex"
    :width            "600px"
    :height           "600px"
    :background-color "#000"
    :border           "3px solid #111"}})
  
(def bpm-selector-style
  {:width "40px"
   :text-align "center"})

(def scale-selector-style
  {:text-align "center"})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Views 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn note [& on?]
  [:div (note-style (first on?))])

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
  [:div seq-container-style
   (map note [nil nil 1 nil nil nil 1 nil nil nil])])

(defn bpm-selector [state]
  [:input {:type "text"
           :style bpm-selector-style
           :value (:tempo @state)
           :on-input #(swap! state assoc :tempo (-> % .-target .-value))}])

(defn play-button [song bpm]
  [:button {:onClick #(audio/play-sequence song bpm)} "PLAY"])

(defn page [state]
  (prn "STATE" @state)
  [:div page-style
   [:div {:style {:color "white"}} (str @state)]
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
