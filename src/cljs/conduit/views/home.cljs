(ns conduit.views.home)

(defn- feed-toggle []
  [:div.feed-toggle
   [:ul.nav.nav-pills.outline-active
    [:li.nav-item [:a.nav-link.disabled {:href ""} "Your Feed"]]
    [:li.nav-item [:a.nav-link.active {:href ""} "Global Feed"]]]])

(defn- article-preview
  [{{:keys [username name image]}               :profile
    {:keys [date likes slug title description]} :article}]
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
    [:span "Read more..."]]])

(defn- tag-pill
  [{:keys [text]}]
  [:a.tag-pill.tag-default {:href ""} text])

(defn sidebar []
  [:div.sidebar
   [:p "Popular Tags"]
   (->> ["programming"
         "javascript"
         "emberjs"
         "angularjs"
         "react"
         "mean"
         "node"
         "rails"]
        (map #(do [tag-pill {:text %}]))
        (into [:div.tag-list]))])

(defn page []
  (let []
    (fn []
      [:div.home-page
       [:div.banner
        [:div.container
         [:h1.logo-font "conduit"]]]
       [:div.container.page
        [:div.row
         [:div.col-md-9
          [feed-toggle]
          [article-preview {:profile {:username "esimons"
                                      :name     "Eric Simmons"
                                      :image    "http://i.imgur.com/Qr71crq.jpg"}
                            :article {:likes       29
                                      :slug        "how-to-build"
                                      :date        "January 20th"
                                      :title       "How to build webapps that scale"
                                      :description "This is the description for the post."}}]]
         [:div.col-md-3
          [sidebar]]]]])))