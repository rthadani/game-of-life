(ns game-of-life.core
  (:require [game-of-life.engine :as engine]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/background 240)
  (q/frame-rate 10)
  (doseq [x (range 0 1000)
          y (range 0 1000)]
    (q/rect (* x 10) (* y 10) 10 10))
  {:current-state engine/gosper-glider-gun
   :previous-state #{}})


(defn update-state [state]
  {:previous-state (:current-state state)
   :current-state (engine/tick (:current-state state))})

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/fill 255 255 255)
  (doseq [[x y] (:previous-state state)]
    (q/rect (* x 10) (* y 10) 10 10))
  (q/fill 0 0 0)
  (doseq [[x y] (:current-state state)]
    (q/rect (* x 10) (* y 10) 10 10)))

(q/defsketch game-of-life
  :title "Evolve you cell you!"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
