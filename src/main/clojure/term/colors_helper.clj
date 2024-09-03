(ns clojure.term.colors-helper
  (:require [clojure.string :as string]))

(defn- escape-code
  [i]
  (str "\033[" i "m"))

(def ^:dynamic *colors*
  "foreground color map"
  (zipmap [:grey :red :green :yellow
           :blue :magenta :cyan :white]
          (map escape-code
               (range 30 38))))

(def ^:dynamic *highlights*
  "background color map"
  (zipmap [:on-grey :on-red :on-green :on-yellow
           :on-blue :on-magenta :on-cyan :on-white]
          (map escape-code
               (range 40 48))))

(def ^:dynamic *attributes*
  "attributes color map"
  (into {}
        (filter (comp not nil? key)
                (zipmap [:bold, :dark, :italic, :underline,
                         :blink, nil, :reverse-color, :concealed]
                        (map escape-code (range 1 9))))))


(defmacro define-color-function
  "define a function `fname' which wraps its arguments with
        corresponding `color' codes"
  [fname color]
  (let [fname (symbol (name fname))
        args (symbol 'args)]
    `(defn ~fname [& ~args]
       (if-not clojure.term.colors/*disable-colors*
         (str (string/join (map #(str ~color %) ~args)) clojure.term.colors/*reset*)
         (apply str ~args)))))

(defmacro define-color-functions-from-map
  "define functions from color maps."
  [colormap]
  `(do ~@(map (fn [[color escape-code]]
                `(define-color-function ~color ~escape-code))
              colormap)))

(defmacro define-functions []
  `(do (define-color-functions-from-map ~*colors*)
       (define-color-functions-from-map ~*highlights*)
       (define-color-functions-from-map ~*attributes*)))

(defmacro export-symbols []
  (let [function-keywords (keys (concat *colors* *highlights* *attributes*))
        symbols (map (comp symbol name) function-keywords)]
    (zipmap function-keywords symbols)))
