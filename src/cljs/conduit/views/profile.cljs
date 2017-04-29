(ns conduit.views.profile
  (:require [conduit.views.components :refer [article-preview]]))

(defn- user-info
  [{:keys [image-url name description username follower-count]}]
  [:div.user-info
   [:div.container
    [:div.row
     [:div.col-xs-12.col-md-10.offset-md-1
      [:img.user-img {:src image-url}]
      [:h4 name]
      [:p description]
      [:button.btn.btn-sm.btn-outline-secondary.action-btn
       [:i.ion-plus-round]
       (str " Follow " name " ")
       [:span.counter (str "(" follower-count ")")]]]]]])

(defn- article-toggle []
  [:div.articles-toggle
   [:ul.nav.nav-pills.outline-active
    [:li.nav-item [:a.nav-link.disabled {:href ""} "My Posts"]]
    [:li.nav-item [:a.nav-link.active {:href ""} "Favorited Posts"]]]])

(defn page []
  (let []
    (fn []
      [:div.profile-page
       [user-info {:name           "Eric Simons"
                   :image-url      "http://i.imgur.com/Qr71crq.jpg"
                   :description    "Cofounder @GoThinkster, lived in Aol's HQ for a few months,
                                 kinda looks like Peeta from the Hunger Games"
                   :username       "esimons"
                   :follower-count 10}]
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