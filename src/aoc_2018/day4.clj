(ns aoc-2018.day4
  (:import
   java.time.OffsetDateTime
   java.time.ZoneOffset)
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as str]))

(def input-pattern #"\[(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})\] (.+)")

(defn part1
  ([]
   (part1 "day4/input.txt"))
  ([input-filename]
   (let [log-entries (->> (io/resource input-filename)
                          (slurp)
                          (str/split-lines)
                          (map (fn [line]
                                 (let [[_ year month day hour minute msg :as v] (re-matches input-pattern line)

                                       date (OffsetDateTime/of (Integer/parseInt year)
                                                               (Integer/parseInt month)
                                                               (Integer/parseInt day)
                                                               (Integer/parseInt hour)
                                                               (Integer/parseInt minute)
                                                               0
                                                               0
                                                               ZoneOffset/UTC)]
                                   [date msg])))
                          (into (sorted-map)))]
     log-entries)))
