(ns conduit.views.settings
  (:require [dommy.core :refer-macros [sel1]]
            [conduit.views.components :as components]
            [re-frame.core :as re-frame]))

(defn page []
  (let [user (re-frame/subscribe [:user])
        update-pending (re-frame/subscribe [:request-pending :update-user!])
        update-errors (re-frame/subscribe [:request-errors :update-user!])]
    [:div.settings-page
     [:div.row
      [:div.col-md-6.offset-md-3.col-xs-12
       [:h1.text-xs-center "Your Settings"]
       [components/error-list @update-errors]
       [:form
        [:fieldset
         [components/large-input {:id "image" :placeholder "URL of profile picture" :default-value (:image @user)}]
         [components/large-input {:id "username" :placeholder "Your Name" :default-value (:username @user)}]
         [components/large-textarea {:id "bio" :placeholder "Short bio about you" :default-value (:bio @user)}]
         [components/large-input {:id "email" :placeholder "Email" :default-value (:email @user)}]
         [components/large-input {:id "password" :type :password :placeholder "Password"}]
         [:button.btn.btn-lg.btn-primary.pull-xs-right
          {:class    (when @update-pending "disabled")
           :on-click #(re-frame/dispatch [:update-user! {:image    (.-value (sel1 :#image))
                                                         :username (.-value (sel1 :#username))
                                                         :bio      (.-value (sel1 :#bio))
                                                         :email    (.-value (sel1 :#email))
                                                         :password (.-value (sel1 :#password))}])}
          (when @update-pending [:i.ion-load-a.spin-icon])
          "Update Settings"]]]]]]))