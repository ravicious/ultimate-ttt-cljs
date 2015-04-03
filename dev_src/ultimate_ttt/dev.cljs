(ns ultimate-ttt.dev
  (:require [ultimate-ttt.core]
            [figwheel.client :as fw]))

(fw/start {:on-jsload (fn [] (print "reloaded"))
           :websocket-url "ws://localhost:3449/figwheel-ws"})

