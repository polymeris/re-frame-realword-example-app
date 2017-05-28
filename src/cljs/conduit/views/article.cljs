(ns conduit.views.article
  (:require [markdown.core :as markdown]
            [re-frame.core :as re-frame]))

; TODO favorite articles
; TODO delete articles
; TODO new comments
; TODO edit comment
; TODO delete comment

(defn- comment-card [{date :date body :body {:keys [username image]} :author}]
  (let [user (re-frame/subscribe [:user])
        mod-options (= (:username @user) username)]
    [:div.card
     [:div.card-block
      [:p.card-text {:dangerouslySetInnerHTML {:__html (markdown/md->html body)}}]]
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

(defn article-meta
  [{created-at                        :date
    likes                             :likes
    favorites-count                   :favoritesCount
    favorited                         :favorited
    slug                              :slug
    {username :username image :image} :author}]
  (let [logged-in? (re-frame/subscribe [:logged-in?])
        unfavorite-pending (re-frame/subscribe [:request-pending :unfavorite-article! slug])
        favorite-pending (re-frame/subscribe [:request-pending :favorite-article! slug])]
    [:div.article-meta
     [:a {:href (str "#/profile/" username)} [:img {:src image}]]
     [:div.info
      [:a.author {:href (str "#/profile/" username)} username]
      [:span.date created-at]]
     [:a.btn.btn-sm.pull-xs-right.loading
      {:class    (if favorited "btn-primary" "btn-outline-primary")
       :href     (if @logged-in? "#" "#/login")
       :on-click #(when @logged-in? (re-frame/dispatch [(if favorited :unfavorite-article! :favorite-article!) slug])
                                    (.preventDefault %))}   ;avoid jumping to the top due to "#" href
      [:i {:class (if (or @unfavorite-pending @favorite-pending)
                    "ion-load-a spin-icon"
                    "ion-heart")}]
      favorites-count likes]]))

(defn article-preview
  [{:keys [slug title description tag-list author] :as article}]
  [:div.article-preview
   {:key slug}
   [article-meta article]
   [:a.preview-link {:href (str "#/article/" slug)}
    [:h1 title]
    [:p description]
    [:span "Read more..."]
    [:ul.tag-list
     (map #(do [:li.tag-default.tag-pill.tag-outline %]) tag-list)]]])

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
          [:p {:dangerouslySetInnerHTML {:__html (markdown/md->html (:body @article))}}]]]
        [:hr]
        [comments @article]]])))