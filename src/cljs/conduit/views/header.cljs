(ns conduit.views.header
  (:require [re-frame.core :as re-frame]))

(defn- navitem
  [{:keys [icon-class text target]}]
  (let [active-page (re-frame/subscribe [:active-page])]
    [:li.nav-item
     [:a.nav-link {:class (when (= @active-page target) "active")
                   :href  (str "#/" (name target))}
      (when icon-class [:i {:class icon-class} " "])
      text]]))

(defn navbar []
  (let [user (re-frame/subscribe [:user])
        active-page (re-frame/subscribe [:active-page])]
    [:nav.navbar.navbar-light
     [:a.navbar-brand {:href "#/"} "conduit"]
     (if @user
       [:ul.nav.navbar-nav.pull-xs-right
        [navitem {:text "Home" :target :home}]
        [navitem {:icon-class :ion-compose :text "New Post" :target :editor}]
        [navitem {:icon-class :ion-gear-a :text "Settings" :target :settings}]
        [:li.nav-item
         [:a.nav-link {:class (when (= @active-page :profile) "active")
                       :href  (str "#/profile/" (:username @user))}
          [:img.user-pic {:src (:image @user)}]
          (:username @user)]]
        [navitem {:text "Log out" :target :logout}]]
       [:ul.nav.navbar-nav.pull-xs-right
        [navitem {:text "Log in" :target :login}]
        [navitem {:text "Sign up" :target :signup}]])]))