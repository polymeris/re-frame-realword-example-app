(ns conduit.views.auth
  (:require [re-frame.core :as re-frame]
            [dommy.core :refer-macros [sel1]]
            [conduit.views.components :refer [large-input error-list]]))

(defn page []
  (let [login-pending (re-frame/subscribe [:request-pending :login!])
        login-errors (re-frame/subscribe [:request-errors :login!])]
    [:div.auth-page
     [:div.container.page
      [:div.row
       [:div.col-md-6.offset-md-3.col-xs-12
        [:h1.text-xs-center "Log in"]
        [:p.text-xs-center [:a {:href "#/signup"} "Need an account?"]]
        [error-list @login-errors]
        [:form
         [large-input {:id "email" :placeholder "Email"}]
         [large-input {:id "password" :type :password :placeholder "Password"}]
         [:button.btn.btn-lg.btn-primary.pull-xs-right
          {:class    (when @login-pending "disabled")
           :on-click #(re-frame/dispatch [:login! {:email    (.-value (sel1 :#email))
                                                   :password (.-value (sel1 :#password))}])}
          (when @login-pending [:i.ion-load-a.spin-icon])
          "Log in"]]]]]]))