(ns aoc-2018.day1
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn part1
  ([]
   (part1 "day1/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (str/split-lines)
        (map #(Integer/parseInt %))
        (reduce +))))

(defn part2
  ([]
   (part2 "day1/input.txt"))
  ([input-filename]
   (loop [found-freqs  #{}
          freq-deltas  (->> (io/resource input-filename)
                            (slurp)
                            (str/split-lines)
                            (map #(Integer/parseInt %))
                            (cycle))
          current-freq 0]
     (let [new-freq (+ current-freq (first freq-deltas))]
       (if (contains? found-freqs new-freq)
         new-freq
         (recur (conj found-freqs new-freq)
                (rest freq-deltas)
                new-freq))))))
