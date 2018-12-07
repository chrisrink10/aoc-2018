(ns aoc-2018.day6
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn manhattan-distance
  [[x1 y1] [x2 y2]]
  (+ (Math/abs (- x1 x2)) (Math/abs (- y1 y2))))

(defn part1
  ([]
   (part1 "day6/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (str/split-lines)
        (into []
              (comp (map #(str/split % #", "))
                    (map (fn [coords]
                           (->> coords
                                (map #(Integer/parseInt %))
                                (vec))))))
        (sort-by #(manhattan-distance % [0 0]))
        (reduce (fn [{:keys [max-x max-y] :as m} [x y :as coords]]
                  (-> (update m :coords conj coords)
                      (update :max-x #(if (> x %) x %))
                      (update :max-y #(if (> y %) y %))))
                {:coords []
                 :max-x  0
                 :max-y  0})
        )))
