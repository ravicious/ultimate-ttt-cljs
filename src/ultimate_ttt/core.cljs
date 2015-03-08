(ns ^:figwheel-always ultimate-ttt.core
  (:require [figwheel.client :as fw]))

(enable-console-print!)

(println "Ready.")

(fw/start {
           :on-jsload (fn [] (print "reloaded"))})
