(ns clojure.term.run-tests
  (:require [clojure.term.colors :as colors]
            [clojure.term.colors-tests :as tests]
            [clojure.test :as clj-test]))

(tests/define-colors-tests color-test)

(defn main [& args]
  (clj-test/run-test color-test))
