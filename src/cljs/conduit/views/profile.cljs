(ns conduit.views.profile
  (:require [conduit.views.components :refer [article-preview]]
            [re-frame.core :as re-frame]))

(defn- user-info
  [{:keys [image bio username follower-count]}]
  (let [logged-in-user (re-frame/subscribe [:user])]
    [:div.user-info
     [:div.container
      [:div.row
       [:div.col-xs-12.col-md-10.offset-md-1
        [:img.user-img {:src image}]
        [:h4 username]
        [:p bio]
        (when (and @logged-in-user (not= (:username @logged-in-user) username))
          [:button.btn.btn-sm.btn-outline-secondary.action-btn
           [:i.ion-plus-round]
           (str " Follow " username " ")
           ;[:span.counter (str "(" follower-count ")")]
           ])]]]]))

(defn- article-toggle []
  [:div.articles-toggle
   [:ul.nav.nav-pills.outline-active
    [:li.nav-item [:a.nav-link.disabled {:href ""} "Posts"]]
    [:li.nav-item [:a.nav-link.active {:href ""} "Favorited Posts"]]]])

(defn page []
  (re-frame/dispatch [:update-active-profile])
  (let [profile (re-frame/subscribe [:active-profile])]
    (fn []
      [:div.profile-page
       [:span @profile]
       [user-info @profile]
       [:div.container
        [:div.row
         [:div.col-xs-12.col-md-10.offset-md-1
          [article-toggle]]
         [article-preview {:profile {:username "esimons"
                                     :name     "Eric Simmons"
                                     :image    "http://i.imgur.com/Qr71crq.jpg"}
                           :article {:likes       29
                                     :slug        "how-to-build"
                                     :date        "January 20th"
                                     :title       "How to build webapps that scale"
                                     :description "This is the description for the post."
                                     :tag-list    ["Music" "Song"]}}]]]])))