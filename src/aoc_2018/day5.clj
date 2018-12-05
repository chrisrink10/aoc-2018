(ns aoc-2018.day5
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn lower?
  [s]
  (= (str/lower-case s) (str s)))

(defn upper?
  [s]
  (= (str/upper-case s) (str s)))

(defn opposite-case?
  [x y]
  (or (and (lower? x) (upper? y))
      (and (lower? y) (upper? x))))

(defn str=
  [s1 s2]
  (= (str/lower-case s1) (str/lower-case s2)))

(defn filter-polymers
  [coll]
  (lazy-seq
   (when (seq coll)
     (let [[x y] coll]
       (if (and x y (str= x y) (opposite-case? x y))
         (filter-polymers (nthrest coll 2))
         (cons x (filter-polymers (rest coll))))))))

(defn cancel-polymers
  [s]
  (let [simplified (into [] (filter-polymers s))]
    (if (= (count s) (count simplified))
      (apply str simplified)
      (recur simplified))))

(defn part1
  ([]
   (part1 "day5/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (drop-last 1)
        (cancel-polymers)
        (count))))

(comment
  (part1)

  )
