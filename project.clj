(defproject eu.cassiel/monome-fu "0.2.3"
  :description "Generic library for talking to monome devices via serialosc"
  :url "https://github.com/cassiel/monome-fu"
  :signing {:gpg-key "nick@cassiel.eu"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[eu.cassiel/clojure-zeroconf "1.2.0"]
                 [net.loadbang/net.loadbang.osc "1.6.1"]
                 [org.clojure/clojure "1.6.0"]])
