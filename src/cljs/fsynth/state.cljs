(ns fsynth.state
  (:require
   [reagent.core :as reagent]))

(def state
  (reagent/atom
   {:mode :lydian
    :tempo 120}))
