(ns armagedom.core
  (:use armagedom.dom)
  (:import [javax.xml.transform
              TransformerFactory
              Transformer])
  (:import javax.xml.transform.dom.DOMSource)
  (:import javax.xml.transform.stream.StreamResult))

(defn xml
  "Main function for generating DOM nodes.
  See Readme for syntax"
  [root xmlns nss & nodes]
   (render
     (apply document (name root) xmlns nss)
     xmlns
     (apply array-map nss)
     nodes))

(defn spit-xml
  "Convert document to XML and write it to out."
  [out document]
  (-> (TransformerFactory/newInstance)
    (.newTransformer)
    (.transform (DOMSource. document)
                (StreamResult.
                  (clojure.java.io/writer out)))))
