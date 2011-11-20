(ns armagedom
  (:require clojure.java.io)
  (:import javax.xml.parsers.DocumentBuilderFactory
           javax.xml.transform.TransformerFactory
           javax.xml.transform.dom.DOMSource
           javax.xml.transform.stream.StreamResult))

(declare ^:dynamic *document* ^:dynamic *nss*)

(defn document
  "Create a new org.w3c.dom.Document
  with a given root node and namespace"
  [root uri]
  (-> (doto (DocumentBuilderFactory/newInstance)
        (.setValidating true)
        (.setNamespaceAware true))
    (.newDocumentBuilder)
    (.getDOMImplementation)
    (.createDocument uri root nil)))

(defn add-namespace [root prefix url]
  (.setAttribute root (str "xmlns:" prefix) url))

(defn add-namespaces [root nss]
  (doseq [[prefix url] nss]
    (add-namespace root prefix url)))

(defprotocol Nodify
  (make-node [this]))

(extend-type org.w3c.dom.Node
  Nodify
  (make-node [this] this)) 

(extend-type clojure.lang.IPersistentCollection
  Nodify
  (make-node [[tag & children :as node]]
    (doseq [[k v] (meta node)]
      (.setAttribute tag (name k) (str v)))
    (doseq [node children]
      (.appendChild tag node))
    tag))

(extend-type clojure.lang.Keyword
  Nodify
  (make-node [this]
    (let [prefix (namespace this)
          tag (name this)
          uri (get *nss* prefix)]
      (if prefix
        (.createElementNS *document* uri (str prefix \: tag))
        (.createElement *document* tag)))))

(extend-type Object
  Nodify
  (make-node [this]
    (.createTextNode *document* this)))

(defn meta-postwalk [f form]
  (let [pf (partial meta-postwalk f)]
    (if (coll? form)
      (f (with-meta (map pf form) (meta form)))
      (f form))))
  
(defn render [xmlseq]
  (meta-postwalk make-node xmlseq))

(defn xml [root uri nss & xmlseq]
  (binding [*document* (document root uri) *nss* nss]
    (let [root (.getDocumentElement *document*)]
      (add-namespaces root nss)
      (render (cons root xmlseq))
      *document*)))

(defn spit-xml
  "Convert document to XML and write it to out."
  [out document]
  (-> (TransformerFactory/newInstance)
    (.newTransformer)
    (.transform (DOMSource. document)
                (StreamResult.
                  (clojure.java.io/writer out)))))

(defn xml-str [document]
  (with-out-str (spit-xml *out* document)))
