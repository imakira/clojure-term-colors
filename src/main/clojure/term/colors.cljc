(ns clojure.term.colors
  (:require [clojure.term.colors-helper :as  helper]
            [clojure.string :as string]))

(def ^:dynamic *reset* (str "\033[" 0 "m"))

;; Bind to true to have the colorize functions not apply coloring to
;; their arguments.
(def ^:dynamic *disable-colors* nil)

(helper/define-functions)

(def exporting-symbols (helper/export-symbols))

(defn generate-exports []
  #?(:clj (throw (Exception. "this function is only used in clojurescript"))
     :cljs (clj->js exporting-symbols)))
