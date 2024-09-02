(ns clojure.term.colors-tests
  (:require [clojure.term.colors :as colors]))

(defn get-fn
  "get function from symbol in clojure.term.colors package"
  [fname]
  (symbol "clojure.term.colors" (-> fname name)))

(defmacro test-colors-from-map
  "test print colors from a color map"
  [colormap & more]
  `(do ~@(map (fn [[color _]]
                `(println (apply ~(get-fn color)
                                 (name ~color) (str ~@more))))
              colormap)))

(defmacro define-colors-tests "" []
  `(clojure.test/deftest ~'color-test
     (clojure.test/testing "Testing colors."
       (test-colors-from-map ~clojure.term.colors-helper/*colors* " foreground.")
       (test-colors-from-map ~clojure.term.colors-helper/*highlights* " background.")
       (test-colors-from-map ~clojure.term.colors-helper/*attributes* " attributes."))

     (clojure.test/testing "Testing disable colors."
       (binding [clojure.term.colors/*disable-colors* true]
         (println  \newline "When disabled-colors is set ...")
         (test-colors-from-map ~clojure.term.colors-helper/*colors* " foreground.")))))

