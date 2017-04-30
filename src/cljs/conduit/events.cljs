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

