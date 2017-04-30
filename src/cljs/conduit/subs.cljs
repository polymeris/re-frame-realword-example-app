(ns conduit.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :active-page
 (fn [db _]
   (:active-page db)))

(re-frame/reg-sub
  :pending-requests
  (fn [db query]
    (get-in db query)))
