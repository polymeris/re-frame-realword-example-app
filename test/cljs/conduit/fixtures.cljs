(ns conduit.fixtures
  (:require [re-frame.core :as re-frame]))

(defn re-frame-state []
  (let [restore-re-frame (atom nil)]
    {:before #(do (reset! restore-re-frame (re-frame/make-restore-fn))
                  (re-frame/reg-event-db
                    :api-request-failure
                    (fn [_ request]
                      (print "API request failed " request))))
     :after  @restore-re-frame}))