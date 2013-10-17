(ns user
  (:require (cassiel.zeroconf [client :as cl])
            (eu.cassiel.monome-fu [network :as n]
                                  [connect :as c]))
  (:import [java.net InetAddress DatagramSocket]
           [net.loadbang.osc.data Message]
           [net.loadbang.osc.comms UDPTransmitter UDPReceiver]
           [net.loadbang.osc.exn CommsException]))

;; --- Basic comms:

(def rx (n/start-receiver 5001 println))

(.close rx)

;; --- checking zeroconf (which we don't actually use any more):

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

;; --- Utility printing

(defn pr-message [^Message m]
  (println (.getAddress m))
  (doseq [i (range (.getNumArguments m))] (println "->" (.getValue (.getArgument m i))))
  )

;; --- Create a socket given a consuming function.

(defn start-receiver [f]
  (let [rx (proxy [UDPReceiver]
                 []
               (consumeMessage [date00 m] (f m)))
        _ (.open rx)]

    (.start (Thread. (reify Runnable
                       (run [this]
                         (try
                           (dorun (repeatedly #(.take rx)))
                           (catch CommsException _ nil)
                           )))))
    rx))



(defn start-transmitter [a key back-port]
  (let [{:keys [server port]} (get (cl/examine a) key)
        tx (UDPTransmitter. (InetAddress/getByName server) port)]

    (.transmit tx
               (-> (Message. "/sys/port")
                   (.addInteger back-port)))

    (.transmit tx
               (-> (Message. "/sys/prefix")
                   (.addString "/blatt")))

    (.transmit tx
               (-> (Message. "/sys/info")
                   (.addString "localhost")
                   (.addInteger back-port)))

    tx
    )

  )

(def rx (start-receiver pr-message))

(def tx (start-transmitter a "monome arc 4 (m0000270)" (.getPort rx)))

(.close rx)
(.close tx)

;; -----

(let [sock (proxy [UDPReceiver]
               []
             (consumeMessage [this date00 m]
               (println m)))
      {:keys [server port]} (get (cl/examine a) "monome arc 4 (m0000270)")
      _ (.open sock)
      in-port (.getPort sock)]
  (println in-port)
  (.close sock)
  )

(get (cl/examine a) "monome arc 4 (m0000270)")
