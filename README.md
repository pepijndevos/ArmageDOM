# ArmageDOM

_The epic battle on the final day of XML. A Clojure DSL similar to Hiccup to generate XML._

## What?

ArmageDOM is a Clojure DSL for generating XML using a list syntax. It supports namespaces.

## How?

project.clj:

    [armagedom "1.0.0-SNAPSHOT"]

## I mean, how?

    user=> (use 'armagedom)
    nil
    user=> (xml :feed "http://www.w3.org/2005/Atom" {"activity" "http://activitystrea.ms/spec/1.0/"}
    user=*      [:activity/verb
    user=*        "status"
    user=*        [:test 1]]
    user=*      ^{:title "foo"} [:item 1 2 3])
    #<DocumentImpl [#document: null]>
    user=> (xml-str *1)
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <feed xmlns="http://www.w3.org/2005/Atom" xmlns:activity="http://activitystrea.ms/spec/1.0/">
        <activity:verb>
            status
            <test>1</test>
        </activity:verb>
        <item title="foo">123</item>
    </feed>
    nil
    user=> (easy-xml :feed "http://example.com" {:foo [{:baz "boo"} {:foo "bar"}]})
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <feed xmlns="http://example.com">
        <foo>
	    <baz>boo</baz>
	    <foo>bar</foo>
	</foo>
    </feed>

(beautified)
