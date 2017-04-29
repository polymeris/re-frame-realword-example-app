(ns conduit.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :as re-frame]))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  (defroute "/" [] (re-frame/dispatch [:set-active-page :home]))
  (defroute "/login" [] (re-frame/dispatch [:set-active-page :auth]))
  (defroute "/register" [] (re-frame/dispatch [:set-active-page :auth]))
  (defroute "/settings" [] (re-frame/dispatch [:set-active-page :settings]))
  (defroute "/editor" [] (re-frame/dispatch [:set-active-page :editor]))
  (defroute "/editor/:slug" [slug] (re-frame/dispatch [:edit-article slug]))
  (defroute "/article/:slug" [slug] (re-frame/dispatch [:view-article slug]))
  (defroute "/profile/:username" [username] (re-frame/dispatch [:view-profile username]))
  (defroute "/profile/:username/favorites" [username] (re-frame/dispatch [:view-favorites username]))
  (hook-browser-navigation!))
