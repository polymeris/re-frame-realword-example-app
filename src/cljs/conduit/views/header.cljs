(ns conduit.views.header
  (:require [re-frame.core :as re-frame]))

(defn- navitem
  [{:keys [icon-class text target]}]
  (let [active-page (re-frame/subscribe [:active-page])]
    [:li.nav-item {:class (when (= @active-page target) "active")}
     [:a {:href (str "#/" (name target))}
      (when icon-class [:i {:class icon-class} " "])
      text]]))

(defn navbar []
  [:nav.navbar.navbar-light
   [:a.navbar-brand {:href "#/"} "conduit"]
   [:ul.nav.navbar-nav.pull-xs-right
    [navitem {:text "Home" :target :home}]
    [navitem {:icon-class :ion-compose :text "New Post" :target :editor}]
    [navitem {:icon-class :ion-gear-a :text "Settings" :target :settings}]
    [navitem {:text "Sign up" :target :auth}]]])