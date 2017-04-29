(ns conduit.views.article
  (:require [conduit.views.components :refer [article-meta]]))

(defn- comment-card [{{:keys [date body]}           :comment
                      {:keys [username name image]} :author
                      mod-options                   :mod-options}]
  [:div.card
   [:div.card-block
    [:p.card-text body]]
   [:div.card-footer
    [:a.comment-author {:href (str "#/profile" username)}
     [:img.comment-author-img {:src image}]]
    " "
    [:a.comment-author {:href (str "#/profile" username)} name]
    [:span.date-posted date]
    (when mod-options
      [:span.mod-options
       [:i.ion-edit]
       [:i.ion-trash-a]])]])

(defn page []
  (let [article {:likes       29
                 :slug        "how-to-build"
                 :date        "January 20th"
                 :title       "How to build webapps that scale"
                 :description "This is the description for the post."
                 :content     "Web development technologies have evolved at an incredible clip over the past few years."
                 :tag-list    ["Music" "Song"]}
        profile {:username "esimons"
                 :name     "Eric Simmons"
                 :image    "http://i.imgur.com/Qr71crq.jpg"}
        comments [{:comment     {:date "Dec 29th"
                                 :body "With supporting text below as a natural lead-in to
                                              additional content."}
                   :author      {:username "jsmith"
                                 :name     "Jacob Smith"
                                 :image    "http://i.imgur.com/Qr71crq.jpg"}
                   :mod-options true}]]
    (fn []
      [:div.article-page
       [:div.banner
        [:div.container
         [:h1 (:title article)]
         [article-meta {:article article :profile profile}]]]
       [:div.container.page
        [:div.row.article-content
         [:div.col-md-12
          [:p (:content article)]]]
        [:div.article-actions
         [article-meta {:article article :profile profile}]]
        [:div.row
         [:div.col-xs-12.col-md-8.offset-md-2
          [:form.card.comment-from
           [:div.card-block]
           [:textarea.form-control {:rows 3 :placeholder "Write a comment..."}]
           [:div.card-footer
            [:img.comment-author-img {:src (:image profile)}]
            [:button.btn.btn-sm.btn-primary.pull-xs-right "Post Comment"]]]
          (map comment-card comments)]]]])))