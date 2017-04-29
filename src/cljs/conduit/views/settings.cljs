(ns conduit.views.settings
  (:require [conduit.views.components :refer [input textarea]]))

(defn page []
  (let []
    (fn []
      [:div.settings-page
       [:div.row
        [:div.col-md-6.offset-md-3.col-xs-12
         [:h1.text-xs-center "Your Settings"]
         [:form
          [:fieldset
           [input {:placeholder "URL of profile picture"}]
           [input {:placeholder "Your Name"}]
           [textarea {:placeholder "Short bio about you"}]
           [input {:placeholder "Email"}]
           [input {:type :password :placeholder "Password"}]
           [:button.btn.btn-lg.btn-primary.pull-xs-right "Update Settings"]]]]]])))