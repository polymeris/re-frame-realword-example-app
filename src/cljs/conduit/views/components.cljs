(ns conduit.views.components)

(defn article-preview
  [{{:keys [username name image]}               :profile
    {:keys [date likes slug title description tag-list]} :article}]
  [:div.article-preview
   [:div.article-meta
    [:a {:href (str "#/profile/" username)} [:img {:src image}]]
    [:div.info
     [:a.author {:href (str "#/profile/" username)} name]
     [:span.date date]]
    [:button.btn.btn-outline-primary.btn-sm.pull-xs-right
     [:i.ion-heart] " " likes]]
   [:a.preview-link {:href (str "#/article/" slug)}
    [:h1 title]
    [:p description]
    [:span "Read more..."]
    [:ul.tag-list
     (map #(do [:li.tag-default.tag-pill.tag-outline %]) tag-list)]]])