(ns fsynth.core
  (:require
   [reagent.core  :as reagent]
   [reagent.dom   :as d]
   [fsynth.audio  :as audio]
   [fsynth.state  :as state]
   [fsynth.update :as update]
   ))

(defn note-brightness [on? state pos]
  (if
      (and (= pos (:index @state))
           (:playing? @state)
           on?)
    "brightness(3)"
    "brightness(1)"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Styles 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def page-style
  {:style
   {:display          "flex"
    :justify-content  "center"
    :height           "100vh"
    :width            "100%" 
    :background-color "black"}})

(defn sequencer-group-style [state]
  (let [zoom? (:zoom? @state)]
    {:style 
     {:transform  (str "scale(" (if zoom? "1)" "0.5)"))
      :position   (if zoom? "absolute" "relative")
      :top        (if zoom? "100px" 0)
      :left       (if zoom? "calc(50% - 200px)" 0)
      :z-index    (if zoom? 10 0)
      :margin     (if zoom? 0 "-120px")
      :transition "all 0.1s ease-out"}}))

(defn note-style [on? state pos]
   {:height           "20px"
    :width            "20px"
    :margin           "10px"
    ;; NOTE: Comment this filter out to imporove performance
    :filter           (note-brightness on? state pos)
    :border-radius    "10px"
    :background-color (if-not on? "#111" (:color @state))})

(def seq-container-style
  {:box-sizing       "border-box"
   :width            "600px"
   :height           "650px"
   :background-color "#000"
   :border           "3px solid #111"})

(def note-container-style
  {:style
   {:display          "flex"}})

(def bpm-selector-style
  {:width            "40px"
   :color            "#fff"
   :border           "1px solid #fff"
   :background-color "#000"
   :padding          "3px"
   :font-size        "10px"
   :text-align       "center"})

(def play-button-style
  {:width         "0"
   :height        "0"
   :font-size     "0"
   :line-height   "0%"
   :margin        "20px"
   :cursor        "pointer"
   :border-top    "15px solid transparent"
   :border-left   "30px solid #333"
   :border-bottom "15px solid transparent"})

(def stop-button-style
  {:width            "30px"
   :height           "30px"
   :margin           "20px"
   :cursor           "pointer"
   :background-color "#333"})

(def clear-button-style
  {:border     "1px solid #fff"
   :color      "#fff"
   :margin     "0 5px"
   :padding    "3px"
   :cursor     "pointer"
   :font-size  "10px"
   :text-align "center"})

(def scale-selector-style
  {:text-align "center"})

(def wave-selector-style
  {:text-align "center"})

(def enlarger-button-style
  {:border     "1px solid #fff"
   :color      "#fff"
   :margin     "0 5px"
   :padding    "3px"
   :cursor     "pointer"
   :font-size  "10px"
   :text-align "center"})

(defn audio-controls-style [state]
  {:style
   {:visibility (if (:zoom? @state) "visible" "hidden")}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Views
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn note [row n state]
  (let [index (keyword (str row))
        notes (-> @state :notes index)
        on? (when (= 1 (nth notes n)) true)]
    [:div {:style (note-style on? state n)
           :id (str row "." n)
           :onClick #(update/update-notes state (-> % .-target .-id))}]))

(defn scale-selector [state]
  [:select {:multiple false
            :style scale-selector-style
            :value (name (:mode @state))
            :on-change #(update/update-mode state (-> % .-target .-value))}
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
            :on-change #(update/update-wave state (-> % .-target .-value))}
   [:option {:value "sine"}     "sine"]
   [:option {:value "triangle"} "triangle"]
   [:option {:value "square"}   "square"]
   [:option {:value "sawtooth"} "sawtooth"]
   ])

(defn sequencer-grid [state]
  [:div {:style   seq-container-style
         :onClick #(if-not (:zoom? @state) (update/enlarge state))}
   (cons :div 
         (mapv (fn [n]
                 [:div note-container-style (map (fn [b] (note n b state)) (range 0 16))])
               (reverse (range 1 17))))])

(defn bpm-selector [state]
  [:input {:type "text"
           :style bpm-selector-style
           :value (:tempo @state)
           :on-input #(update/update-tempo state (-> % .-target .-value))}])

(defn play-button [state]
  [:div {:style (if (:playing? @state) stop-button-style play-button-style)
         :onClick #(do (update/update-playing-state state)
                       (audio/play-all-notes state))}])

(defn clear-button [state]
  [:span {:style clear-button-style
         :onClick #(do (update/clear-all-notes state))} "CLEAR"])

(defn enlarger-button [state]
  [:span {:style enlarger-button-style
          :onClick #(update/enlarge state)} "MINIMIZE"])

(defn audio-controls [state]
  [:div (audio-controls-style state)
   (scale-selector state)
   (wave-selector state)
   (enlarger-button state)
   (clear-button state)
   (bpm-selector state)])

(defn sequencer-group [state]
  [:div (sequencer-group-style state)
   (sequencer-grid state)
   (play-button state)
   (audio-controls state)])

(defn play-button-label [state]
  (if (some? (filter (fn [s] (false? (:playing @s))) state))
    "PLAY"
    "STOP"))

(defn page []
  [:div page-style
   ;; [:div {:style {:color "white"}} (with-out-str (prn (mapv (fn [s] (:playing? @s)) state/all-states)))]
   [:button {:onClick #(audio/master-play) :style {:color "#fff" :height 25 :background-color "#333"}} (play-button-label state/all-states)]
   (sequencer-group state/state1)
   (sequencer-group state/state2)
   (sequencer-group state/state3)
   (sequencer-group state/state4)])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    ))

(defn mount-root []
  (d/render [page] (.getElementById js/document "app")))

(defn init! []
  (dev-setup)
  (mount-root))
