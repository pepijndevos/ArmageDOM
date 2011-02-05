(ns armagedom.dom
  (:import [javax.xml.parsers
              DocumentBuilderFactory
              DocumentBuilder])
  (:require clojure.java.io))

(defn document
  "Create a new org.w3c.dom.Document
  with a given root node, default namespace
  and optional prefix/url pairs."
  [root uri & nss]
  (let [d (-> (doto (DocumentBuilderFactory/newInstance)
                (.setValidating true)
                (.setNamespaceAware true))
            (.newDocumentBuilder)
            (.getDOMImplementation)
            (.createDocument uri root nil))]
    (doseq [[prefix url] (partition 2 nss)]
      (.setAttribute
        (.getDocumentElement d)
        (str "xmlns:" prefix) url))
    d))

(defn render [document xmlns nss nodes]
  (letfn [(make-node [root [tag & nodes]]
            (let [prefix (namespace tag)
                  tag (name tag)]
              (let [ele (if-let [uri (and prefix (get nss prefix))]
                          (.createElementNS document uri (str prefix \: tag))
                          (.createElement document tag))]
                (doseq [child nodes]
                  (inner-render ele child))
                ele)))

          (inner-render [root node]
            (cond
              (coll? node) (.appendChild root (make-node root node))
              (string? node) (.appendChild root (.createTextNode document node))
              (instance? org.w3c.dom.Element node) (.appendChild root node)
              (fn? node) node
              :else (recur root (str node))))]

    (let [root (.getDocumentElement document)]
      (doseq [node nodes]
        (.appendChild root (inner-render root node))))
    document))
