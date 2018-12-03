(ns aoc-2018.core)

(defn -main [& [which-ns & args]]
  (let [[sym-ns sym-name] (-> (read-string which-ns)
                              ((juxt namespace name)))
        ns-sym            (let [s (symbol sym-ns)]
                            (require s)
                            s)
        v                 (ns-resolve (the-ns ns-sym)
                                      (symbol sym-name))]
    (println (apply v args))))
