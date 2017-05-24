(ns conduit.views.article
  (:require [conduit.views.components :refer [article-meta]]
            [re-frame.core :as re-frame]))

(defn- comment-card [{date :date body :body {:keys [username image]} :author}]
  (let [user (re-frame/subscribe [:user])
        mod-options (= (:username @user) username)]
    [:div.card
     [:div.card-block
      [:p.card-text body]]
     [:div.card-footer
      [:a.comment-author {:href (str "#/profile" username)}
       [:img.comment-author-img {:src image}]]
      " "
      [:a.comment-author {:href (str "#/profile" username)} username]
      [:span.date-posted date]
      (when mod-options
        [:span.mod-options
         [:i.ion-edit]
         [:i.ion-trash-a]])]]))

(defn update-article-comments [article]
  (re-frame/dispatch [:get-comments article]))

(defn comments [article]
  (update-article-comments article)
  (let [user (re-frame/subscribe [:user])]
    [:div.row
     [:div.col-xs-12.col-md-8.offset-md-2
      [:form.card.comment-form
       [:div.card-block]
       [:textarea.form-control {:rows 3 :placeholder "Write a comment..."}]
       [:div.card-footer
        [:img.comment-author-img {:src (:image @user)}]
        [:button.btn.btn-sm.btn-primary "Post Comment"]]]
      (map comment-card (:comments article))]]))

(defn page []
  (let [article (re-frame/subscribe [:active-article])]
    (fn []
      [:div.article-page
       [:div.banner
        [:div.container
         [:h1 (:title @article)]
         [article-meta @article]]]
       [:div.container.page
        [:div.row.article-content
         [:div.col-md-12
          [:p (:body @article)]]]
        [:hr]
        [comments @article]]])))