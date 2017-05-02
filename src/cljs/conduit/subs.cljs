(ns conduit.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :active-page
  (fn [db _]
    (:active-page db)))

(reg-sub
  :request-errors
  (fn [db [_ & query]]
    (let [status (get-in db (into [:pending-requests] query))]
      (when (and (vector? status)
                 (= :failed (first status)))
        (second status)))))

(reg-sub
  :request-pending
  (fn [db [_ & query]]
    (= :pending (get-in db (into [:pending-requests] query)))))

(reg-sub
  :user
  (fn [db _]
    (:user db)))

(reg-sub
  :tags
  (fn [db _]
    (:tags db)))
