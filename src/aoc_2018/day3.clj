(ns aoc-2018.day3
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as str]))

(def input-pattern #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

(defn part1
  ([]
   (part1 "day3/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (str/split-lines)
        (map (fn [line]
               (let [[id left top width height] (some->> (re-matches input-pattern line)
                                                         (rest)
                                                         (map #(Integer/parseInt %)))]
                 {:id     id
                  :left   left
                  :top    top
                  :width  width
                  :height height})))
        (mapcat (fn [{:keys [left top width height]}]
                  (->> (range left (+ left width))
                       (mapcat (fn [x]
                                 (map (fn [y]
                                        [x y])
                                      (range top (+ top height))))))))
        (reduce (fn [m point]
                  (if (contains? m point)
                    (update m point inc)
                    (assoc m point 1)))
                {})
        (reduce-kv (fn [sum _ v]
                     (cond-> sum
                       (> v 1) inc))
                   0))))

(defn part2
  ([]
   (part2 "day3/input.txt"))
  ([input-filename]
   (let [points  (->> (io/resource input-filename)
                      (slurp)
                      (str/split-lines)
                      (map (fn [line]
                             (let [[id left top width height] (some->> (re-matches input-pattern line)
                                                                       (rest)
                                                                       (map #(Integer/parseInt %)))]
                               {:id     id
                                :left   left
                                :top    top
                                :width  width
                                :height height})))
                      (mapcat (fn [{:keys [id left top width height]}]
                                (->> (range left (+ left width))
                                     (mapcat (fn [x]
                                               (map (fn [y]
                                                      [[x y] id])
                                                    (range top (+ top height)))))))))
         id-idx  (reduce (fn [m [point id]]
                           (if (contains? m id)
                             (update m id conj point)
                             (assoc m id #{point})))
                         {}
                         points)
         pts-idx (reduce (fn [m [point id]]
                           (if (contains? m point)
                             (update m point conj id)
                             (assoc m point #{id})))
                         {}
                         points)]
     (first
      (reduce (fn [s [id points]]
                (if (every? #(and (= 1 (count (get pts-idx %)))
                                  (= id (first (get pts-idx %))))
                            points)
                  (conj s id)
                  s))
              #{}
              id-idx)))))
