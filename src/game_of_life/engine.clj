(ns game-of-life.engine
  (:require [clojure.core.async :refer [go-loop >!!]]
            [clojure.set :as set]))

(def neighbor-offsets
  (for [x (range -1 2)
        y (range -1 2)
        :let [offset [x y]]
        :when (not= offset [0 0])]
    offset))

(defn neighbors
  [cell]
  (into #{} (map #(mapv + % cell) neighbor-offsets)))

(defn live-neighbors
  [cell current-state]
  (->> (neighbors cell)
       (set/intersection current-state)))

(defn dead-neighbors
  [cell current-state]
  (->> (neighbors cell)
       (filter #(not (contains? current-state %)))))

(defn survivors
  [current-state]
  (into #{}
        (for [cell current-state
              :let [alive (count (live-neighbors cell current-state))]
              :when (or (= alive 2) (= alive 3))]
          cell)))

(defn offspring
  [current-state]
  (into #{}
        (for [cell current-state
              child (dead-neighbors cell current-state)
              :when (= 3 (count (live-neighbors child current-state)))]
          child)))

(defn tick
  [current-state]
  (set/union (survivors current-state) (offspring current-state)))

(defn make-coordinates-from-wiki
  [string-pattern]
  (let [chars (vec (map vec (clojure.string/split-lines string-pattern)))]
    (->> (for [y (range 0 (count chars))
               x (range 0 (count (chars y)))
               :when (= ((chars y) x) \O)]
           [x y])
         (sort-by first)
         (into #{}))))



(def glider (make-coordinates-from-wiki ".O\n..O\nOOO"))
(def lwss (into #{} (map (fn [[x y]] [(+ x 50) (+ y 10)]) (make-coordinates-from-wiki ".O..O\nO\nO...O\nOOOO"))))
(def pulsar (make-coordinates-from-wiki "..OOO...OOO\n\nO....O.O....O\nO....O.O....O\nO....O.O....O\n..OOO...OOO\n\n..OOO...OOO\nO....O.O....O\nO....O.O....O\nO....O.O....O\n\n..OOO...OOO"))
(def blinker (make-coordinates-from-wiki "\nOOO"))
(def gosper-glider-gun (make-coordinates-from-wiki "........................O\n......................O.O\n............OO......OO............OO\n...........O...O....OO............OO\nOO........O.....O...OO\nOO........O...O.OO....O.O\n..........O.....O.......O\n...........O...O\n............OO"))
