(ns aoc-2018.day4
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as str]))

(def input-pattern #"\[(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})\] (.+)")

(def guard-msg-pattern #"Guard #(\d+) begins shift")

(defn guard-partition
  "Partition-by function for statefully splitting sorted log-lines into groups by
  the current guard on shift."
  []
  (let [current-guard (atom nil)]
    (fn [[[year month day hour] msg]]
      (if-let [[_ id] (re-matches guard-msg-pattern msg)]
        (reset! current-guard [year month day hour id])
        @current-guard))))

(defn part1
  ([]
   (part1 "day4/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (str/split-lines)
        (map (fn [line]
               (let [[_ year month day hour minute msg :as v] (re-matches input-pattern line)

                     date [(Integer/parseInt year)
                           (Integer/parseInt month)
                           (Integer/parseInt day)
                           (Integer/parseInt hour)
                           (Integer/parseInt minute)]]
                 [date msg])))
        (into (sorted-map))
        (partition-by (guard-partition))
        (map (fn [logs]
               (let [[_ guard-id] (->> (first logs)
                                       (second)
                                       (re-matches guard-msg-pattern))]
                 [guard-id
                  (->> (drop 1 logs)
                       (map (fn [[[_ _ _ _ minute] msg]]
                              (condp = msg
                                "falls asleep" [:fell-asleep minute]
                                "wakes up"     [:woke-up minute])))
                       (sort-by second)
                       (reduce (fn [m [event-type minute]]
                                 (if (= event-type :fell-asleep)
                                   (assoc m :fell-asleep-min minute)
                                   (let [fell-asleep-min  (:fell-asleep-min m)
                                         total-asleep-min (- minute fell-asleep-min)
                                         mins-asleep      (concat (:mins-asleep m) (range fell-asleep-min minute))]
                                     (-> m
                                         (update :asleep-duration #(cond-> total-asleep-min % (+ %)))
                                         (dissoc :fell-asleep-min)
                                         (assoc :mins-asleep mins-asleep)))))
                               {}))])))
        (reduce (fn [m [id {:keys [asleep-duration mins-asleep] :as details}]]
                  (->> (fn [{prev-asleep-duration :asleep-duration prev-mins-asleep :mins-asleep}]
                         {:id              id
                          :asleep-duration (+ (or prev-asleep-duration 0) (or asleep-duration 0))
                          :mins-asleep     (reduce (fn [m minute]
                                                     (update m minute #(if % (inc %) 1)))
                                                   (or prev-mins-asleep {})
                                                   mins-asleep)})
                       (update m id)))
                {})
        (vals)
        (apply max-key :asleep-duration)
        ((fn [{:keys [id asleep-duration mins-asleep]}]
           (* (Integer/parseInt id) (key (apply max-key val mins-asleep))))))))



