(ns conduit.views.auth
  (:require [conduit.views.components :refer [large-input]]))

(defn page []
  (let []
    (fn []
      [:div.auth-page
       [:div.container.page
        [:div.row
         [:div.col-md-6.offset-md-3.col-xs-12
          [:h1.text-xs-center "Sign up"]
          [:p.text-xs-center [:a {:href ""} "Have an account?"]]
          [:ul.error-messages
           [:li "That email is already taken"]]
          [:form
           [large-input {:placeholder "Your Name"}]
           [large-input {:placeholder "Email"}]
           [large-input {:type :password :placeholder "Password"}]
           [:buton.btn.btn-lg.btn-primary.pull-xs-right "Sign up"]]]]]])))