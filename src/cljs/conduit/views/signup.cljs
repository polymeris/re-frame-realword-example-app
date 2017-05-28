(ns conduit.views.signup
  (:require [re-frame.core :as re-frame]
            [dommy.core :refer-macros [sel1]]
            [conduit.views.components :as components]))

(defn page []
  (let [register-pending (re-frame/subscribe [:request-pending :register!])
        register-errors (re-frame/subscribe [:request-errors :register!])]
    [:div.signup-page
     [:div.container.page
      [:div.row
       [:div.col-md-6.offset-md-3.col-xs-12
        [:h1.text-xs-center "Sign up"]
        [:p.text-xs-center [:a {:href "#/auth"} "Have an account?"]]
        [components/error-list @register-errors]
        [:form
         [components/large-input {:id "username" :placeholder "Your Name"}]
         [components/large-input {:id "email" :placeholder "Email"}]
         [components/large-input {:id "password" :type :password :placeholder "Password"}]
         [:button.btn.btn-lg.btn-primary.pull-xs-right
          {:class    (when @register-pending "disabled loading")
           :on-click #(re-frame/dispatch [:register! {:username (.-value (sel1 :#username))
                                                      :email    (.-value (sel1 :#email))
                                                      :password (.-value (sel1 :#password))}])}
          "Sign up"]]]]]]))
