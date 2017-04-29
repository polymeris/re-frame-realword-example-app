(ns conduit.views.home
  (:require [conduit.views.components :refer [article-preview]]))

(defn- feed-toggle []
  [:div.feed-toggle
   [:ul.nav.nav-pills.outline-active
    [:li.nav-item [:a.nav-link.disabled {:href ""} "Your Feed"]]
    [:li.nav-item [:a.nav-link.active {:href ""} "Global Feed"]]]])

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