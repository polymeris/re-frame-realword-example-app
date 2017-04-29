(ns conduit.views.footer)

(defn footer []
  [:footer
   [:div.container
    [:a.logo-font {:href "/"} "conduit"]
    [:span.attribution
     "An interactive learning project from <a href=\"https://thinkster.io\">Thinkster</a>.
      Code &amp; design licensed under MIT."]]])