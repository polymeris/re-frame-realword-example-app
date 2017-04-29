(ns conduit.views.auth)

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
           [:fieldset.form-group
            [:input.form-control.form-control-lg {:type :text
                                                  :placeholder "Your Name"}]]
           [:fieldset.form-group
            [:input.form-control.form-control-lg {:type :text
                                                  :placeholder "Email"}]]
           [:fieldset.form-group
            [:input.form-control.form-control-lg {:type :password
                                                  :placeholder "Password"}]]
           [:buton.btn.btn-lg.btn-primary.pull-xs-right "Sign up"]]]]]])))