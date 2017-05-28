(ns conduit.views.profile
  (:require [conduit.views.article :as article]
            [re-frame.core :as re-frame]))

; TODO list articles
; TODO list favorites

(defn- profile-info
  [{:keys [image bio username following follower-count]}]
  (let [user (re-frame/subscribe [:user])
        unfollow-pending (re-frame/subscribe [:request-pending :unfollow-profile! username])
        follow-pending (re-frame/subscribe [:request-pending :follow-profile! username])]
    [:div.user-info
     [:div.container
      [:div.row
       [:div.col-xs-12.col-md-10.offset-md-1
        [:img.user-img {:src image}]
        [:h4 username]
        [:p bio]
        (when (and @user (not= (:username @user) username))
          [:button.btn.btn-sm.action-btn
           {:class    (if following "btn-secondary" "btn-outline-secondary")
            :on-click #(re-frame/dispatch [(if following :unfollow-profile! :follow-profile!) username])}
           [:i {:class (cond
                         (or @unfollow-pending @follow-pending) "ion-load-a spin-icon"
                         (not following) "ion-plus-round"
                         :else "ion-minus-round")}]
           (str (if following "Unfollow " "Follow ") username)])]]]]))

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
       [profile-info @profile]
       [:div.container
        [:div.row
         [:div.col-xs-12.col-md-10.offset-md-1
          [article-toggle]]
         [article/article-preview {:profile {:username "esimons"
                                             :name     "Eric Simmons"
                                             :image    "http://i.imgur.com/Qr71crq.jpg"}
                                   :article {:likes       29
                                             :slug        "how-to-build"
                                             :date        "January 20th"
                                             :title       "How to build webapps that scale"
                                             :description "This is the description for the post."
                                             :tag-list    ["Music" "Song"]}}]]]])))