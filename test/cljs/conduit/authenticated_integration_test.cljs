(ns conduit.authenticated-integration-test
  (:require [cljs.test :refer-macros [deftest testing is async use-fixtures]]
            [re-frame.core :refer [dispatch dispatch-sync reg-event-db]]
            [cljs.spec :as spec]
            [conduit.schema :as schema]
            [conduit.fixtures :as fixtures]))

(use-fixtures :each (fixtures/re-frame-state))

(defn with-login [f]
  (reg-event-db
    :login-success
    (fn [db [_ {user :user}]]
      (print "Logged in")
      (f)
      (assoc db :user user)))
  (dispatch [:login! {:email    "re-frame-test@example.com"
                      :password "re-frame-test"}]))

(deftest get-user
  (async done
    (reg-event-db
      :get-user-success
      (fn [_ [_ {:keys [user] :as response}]]
        (print "response was" response)
        (is (spec/valid? ::schema/user user))
        (done)))
    (with-login #(dispatch [:get-user]))))

(deftest create-article
  (async done
    (reg-event-db
      :create-article-success
      (fn [_ [_ _ {:keys [article] :as response}]]
        (print "response was" response)
        (is (spec/valid? ::schema/article article))
        (done)))
    (with-login #(dispatch [:create-article! {:title "Test article"
                                              :body  "Never mind me"}]))))