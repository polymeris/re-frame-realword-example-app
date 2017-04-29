(ns conduit.events
    (:require [re-frame.core :as re-frame]
              [conduit.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :set-active-page
 (fn [db [_ active-panel]]
   (assoc db :active-page active-panel)))
