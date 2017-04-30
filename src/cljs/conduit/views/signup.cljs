(ns conduit.views.signup
  (:require [re-frame.core :as re-frame]
            [dommy.core :refer-macros [sel1]]
            [conduit.views.components :refer [large-input]]))

(defn page []
  (let [signing-up (re-frame/subscribe [:pending-requests :register!])]
    (fn []
      [:div.signup-page
       [:div.container.page
        [:div.row
         [:div.col-md-6.offset-md-3.col-xs-12
          [:h1.text-xs-center "Sign up"]
          [:p.text-xs-center [:a {:href "#/auth"} "Have an account?"]]
          (when (= :failed @signing-up)
            [:ul.error-messages
             [:li "That email is already taken"]])          ;FIXME actual errors
          [:form
           [large-input {:id "username" :placeholder "Your Name"}]
           [large-input {:id "email" :placeholder "Email"}]
           [large-input {:id "password" :type :password :placeholder "Password"}]
           [:buton.btn.btn-lg.btn-primary.pull-xs-right
            {:class    (when (= :pending @signing-up) "disabled")
             :on-click #(re-frame/dispatch [:register! {:username (.-value (sel1 :#username))
                                                        :email    (.-value (sel1 :#email))
                                                        :password (.-value (sel1 :#password))}])}
            "Sign up"]]]]]])))
