(ns aoc-2018.day2
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn letter-index
  [s]
  (reduce (fn [idx c]
            (if (contains? idx c)
              (update idx c inc)
              (assoc idx c 1)))
          {}
          s))

(defn part1
  ([]
   (part1 "day2/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (str/split-lines)
        (map (comp set
                   vals
                   letter-index
                   str/trim))
        (reduce (fn [m counts]
                  (cond-> m
                    (contains? counts 2) (update :twos inc)
                    (contains? counts 3) (update :threes inc)))
                {:twos   0
                 :threes 0})
        ((juxt :twos :threes))
        (reduce *))))

(defn part2
  ([]
   (part2 "day2/input.txt"))
  ([input-filename]
   (let [sub-ids (fn [s]
                   (map-indexed (fn [idx _]
                                  (let [[start end] (split-at idx s)]
                                    (->> (drop 1 end)
                                         (apply str)
                                         (vector (apply str start)))))
                                s))]
     (loop [id-seq  (->> (io/resource input-filename)
                         (slurp)
                         (str/split-lines)
                         (mapcat sub-ids))
            subsets #{}]
       (let [id-tuple (first id-seq)]
         (if (contains? subsets id-tuple)
           (apply str id-tuple)
           (recur (rest id-seq)
                  (conj subsets id-tuple))))))))
