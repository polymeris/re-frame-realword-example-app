(ns conduit.views.settings
  (:require [conduit.views.components :refer [large-input large-textarea]]))

(defn page []
  (let []
    (fn []
      [:div.settings-page
       [:div.row
        [:div.col-md-6.offset-md-3.col-xs-12
         [:h1.text-xs-center "Your Settings"]
         [:form
          [:fieldset
           [large-input {:placeholder "URL of profile picture"}]
           [large-input {:placeholder "Your Name"}]
           [large-textarea {:placeholder "Short bio about you"}]
           [large-input {:placeholder "Email"}]
           [large-input {:type :password :placeholder "Password"}]
           [:button.btn.btn-lg.btn-primary.pull-xs-right "Update Settings"]]]]]])))