(ns conduit.events-test
  (:require [cljs.test :refer-macros [deftest testing is async]]
            [re-frame.core :refer [dispatch reg-event-db]]
            [conduit.events]))


(deftest login-failure
  (async done
    (reg-event-db :login-failure done)
    (dispatch [:login! nil])))

(deftest login-success
  (async done
    (reg-event-db :login-success done)
    (dispatch [:login! {:email "re-frame-test@example.com" :password "re-frame-test"}])))