(ns user
  (:require (cassiel.zeroconf [client :as cl]))
  (:import [java.net DatagramSocket]
           [net.loadbang.osc.comms UDPReceiver]))

;; --- checking zeroconf:

(def a (cl/listen "_monome-osc._udp.local."))

(cl/examine a)

(cl/close a)

;; --- datagram sockets:

(def s (DatagramSocket.))

(.getLocalPort s)

;; --- net.loadbang.osc support:

(def u (proxy [UDPReceiver]
           []
         (consumeMessage [this date00 m]
           (println m))))

(.open u)
(.getPort u)
(.close u)
