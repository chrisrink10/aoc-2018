(defproject aoc-2018 "0.1.0-SNAPSHOT"
  :description "Advent of Code 2018"
  :url "http://github.com/chrisrink10/aoc-2018"
  :license {:name "MIT License"}

  :dependencies [[org.clojure/clojure "1.9.0"]]

  :main aoc-2018.core

  :profiles {:dev     {:dependencies   [[org.clojure/tools.namespace "0.2.11"]
                                        [org.clojure/tools.trace "0.7.9"]]
                       :repl-options   {:init-ns aoc-2018.core
                                        :init    (set! *print-length* 50)}}})
