(ns hiccup-util.core)

(defn classnames [classes-map]
  (->>
    classes-map
    (filter #(true? (second %)))
    (map first)
    (map name)
    (interpose " ")
    (apply str)))
