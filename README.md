# ArmageDOM

_The epic battle on the final day of XML. A Clojure DSL similar to Hiccup to generate XML._

## What?

ArmageDOM is a Clojure DSL for generating XML using a list syntax. It supports namespaces.

## How?

    git clone git://github.com/pepijndevos/ArmageDOM.git
    cake/lein deps

## I mean, how?

    user=> (use 'armagedom.core)
    nil
    user=> (xml :feed "http://www.w3.org/2005/Atom" ["activity" "http://activitystrea.ms/spec/1.0/"]
    user=*      [:activity/verb
    user=*        "status"
    user=*        [:test 1]]
    user=*      (with-meta [:item 1 2 3] {:title "foo"}))
    #<DocumentImpl [#document: null]>
    user=> (let [s (new java.io.StringWriter)] (spit-xml s *1) (println (str s)))
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <feed xmlns="http://www.w3.org/2005/Atom" xmlns:activity="http://activitystrea.ms/spec/1.0/">
        <activity:verb>
            status
            <test>1</test>
        </activity:verb>
        <item title="foo">123</item>
    </feed>
    nil

(beautified)
