(ns conduit.views.home
  (:require [conduit.views.article :as article]
            [re-frame.core :as re-frame]))

(defn- feed-toggle []
  [:div.feed-toggle
   [:ul.nav.nav-pills.outline-active
    [:li.nav-item [:a.nav-link.disabled {:href ""} "Your Feed"]]
    [:li.nav-item [:a.nav-link.active {:href ""} "Global Feed"]]]])

(defn- tag-pill
  [{:keys [text]}]
  [:a.tag-pill.tag-default {:key text :href ""} text])

(defn sidebar []
  (let [popular-tags (re-frame/subscribe [:popular-tags])]
    [:div.sidebar
     [:p "Popular Tags"]
     (->> @popular-tags
          (sort)
          (map #(do [tag-pill {:text %}]))
          (into [:div.tag-list]))]))

(defn page []
  (let [articles (re-frame/subscribe [:articles])]
    (re-frame/dispatch [:get-articles])
    (fn []
      [:div.home-page
       [:div.banner
        [:div.container
         [:h1.logo-font "conduit"]]]
       [:div.container.page
        [:div.row
         [:div.col-md-9
          [feed-toggle]
          (map article/article-preview @articles)]
         [:div.col-md-3
          [sidebar]]]]])))