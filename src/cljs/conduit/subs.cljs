(ns conduit.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :active-page
  (fn [db _]
    (:active-page db)))

(reg-sub
  :pending-requests
  (fn [db query]
    (get-in db query)))

(reg-sub
  :user
  (fn [db _]
    (:user db)))
