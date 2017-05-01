(ns conduit.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [json-request-format json-response-format]]
            [clojure.string :as string]
            [conduit.db :as db]))

(def base-url "https://conduit.productionready.io")

(defn uri [& path]
  (string/join "/" (concat [base-url "api"] path)))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ active-panel]]
    (assoc db :active-page active-panel)))

(reg-event-fx
  :login!
  (fn [{:keys [db]} [_ user]]
    {:db         (assoc-in db [:pending-requests :login!] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "users/login")
                  :params          {:user user}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:login-success]
                  :on-failure      [:login-failure]}}))

(reg-event-db
  :login-success
  (fn [db [_ {user :user}]]
    (dispatch [:set-active-page :main])
    (-> db
        (assoc-in [:pending-requests :login!] false)
        (assoc :user user))))

(reg-event-db
  :login-failure
  (fn [db _]
    (assoc-in db [:pending-requests :login!] :failed)))


(reg-event-db
  :logout!
  (fn [db _]
    (dissoc db :user)))

(reg-event-fx
  :register!
  (fn [{:keys [db]} [_ user]]
    {:db         (assoc-in db [:pending-requests :register!] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "users")
                  :params          {:user user}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:register-success]
                  :on-failure      [:register-failure]}}))

(reg-event-db
  :register-success
  (fn [db [_ {user :user}]]
    (dispatch [:set-active-page :main])
    (-> db
        (assoc-in [:pending-requests :register!] false)
        (assoc :user user))))

(reg-event-db
  :register-failure
  (fn [db _]
    (assoc-in db [:pending-requests :register!] :failed)))


(reg-event-fx
  :get-user
  (fn [{:keys [db]} [_ user]]
    {:http-xhrio {:method          :get
                  :uri             (uri "user")
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-user-success]}}))

(reg-event-db
  :get-user-success
  (fn [db [_ user]]
    (assoc db :user user)))

(reg-event-fx
  :update-user!
  (fn [{:keys [db]} user]
    {:db         (assoc-in db [:pending-requests :update-user!] :pending)
     :http-xhrio {:method          :put
                  :uri             (uri "users")
                  :params          {:user user}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:update-user-success]
                  :on-failure      [:update-user-failure]}}))

(reg-event-db
  :update-user-success
  (fn [db [_ user]]
    (-> db
        (assoc-in [:pending-requests :update-user!] false)
        (assoc :user user))))

(reg-event-db
  :update-user-failure
  (fn [db _]
    (assoc-in db [:pending-requests :update-user!] :failed)))

(reg-event-fx
  :get-profile
  (fn [{:keys [db]} username]
    {:http-xhrio {:method          :get
                  :uri             (uri "profiles" username)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-profile-success username]}}))

(reg-event-db
  :get-profile-success
  (fn [db [_ username profile]]
    (assoc-in db [:profiles username] profile)))

(reg-event-fx
  :follow-profile!
  (fn [{:keys [db]} username]
    {:db         (assoc-in db [:pending-requests :follow-profile!] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "profiles" username "follow")
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:follow-profile-success username]
                  :on-failure      [:follow-profile-failure username]}}))

(reg-event-db
  :follow-profile-success
  (fn [db [_ profile]]
    (-> db
        (assoc-in [:pending-requests :follow-profile!] false)
        ; FIXME add to followers
        )))

(reg-event-db
  :follow-profile-failure
  (fn [db _]
    (assoc-in db [:pending-requests :follow-profile!] :failed)))

(reg-event-fx
  :unfollow-profile!
  (fn [{:keys [db]} username]
    {:db         (assoc-in db [:pending-requests :unfollow-profile! username] :pending)
     :http-xhrio {:method          :delete
                  :uri             (uri "profiles" username "follow")
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:unfollow-profile-success username]
                  :on-failure      [:unfollow-profile-failure username]}}))

(reg-event-db
  :unfollow-profile-success
  (fn [db [_ {username :username}]]
    (-> db
        (assoc-in [:pending-requests :unfollow-profile! username] false)
        ; FIXME add to followers
        )))

(reg-event-db
  :unfollow-profile-failure
  (fn [db [_ {username :username}]]
    (assoc-in db [:pending-requests :unfollow-profile! username] :failed)))