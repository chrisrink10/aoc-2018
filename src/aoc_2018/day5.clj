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

(defn cancel-polymers
  [s]
  (loop [sseq  (seq s)
         final (transient [])]
    (if (seq sseq)
      (let [prev (get final (dec (count final)))
            [x y] sseq]
        (if (and x y (str= x y) (opposite-case? x y))
          (if prev
            (recur (cons prev (nthrest sseq 2)) (pop! final))
            (recur (nthrest sseq 2) final))
          (recur (rest sseq) (conj! final x))))
      (apply str (persistent! final)))))

(defn part1
  ([]
   (part1 "day5/input.txt"))
  ([input-filename]
   (->> (io/resource input-filename)
        (slurp)
        (drop-last 1)
        (cancel-polymers)
        (count))))

(defn part2
  ([]
   (part2 "day5/input.txt"))
  ([input-filename]
   (let [input           (->> (io/resource input-filename)
                              (slurp)
                              (drop-last 1))
         unique-polymers (into #{} (map str/lower-case) input)]
     (->> unique-polymers
          (map (fn [lower]
                 (let [upper (str/upper-case lower)]
                   (->> input
                        (remove (fn [c]
                                  (let [s (str c)]
                                    (or (= upper (str s)) (= lower (str s))))))
                        (cancel-polymers)
                        (count)
                        (vector lower)))))
          (apply min-key second)
          (second)))))

(comment
  (part1)

  )
