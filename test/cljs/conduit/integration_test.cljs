(ns conduit.integration-test
  (:require [cljs.test :refer-macros [deftest testing is async use-fixtures]]
            [re-frame.core :as re-frame :refer [dispatch reg-event-db]]
            [cljs.spec :as spec]
            [conduit.schema :as schema]
            [conduit.events]
            [conduit.fixtures :as fixtures]))

(def random-user-number (rand-int 1e9))

(def test-user {:username (str "re-frame test user" random-user-number)
                :email    (str "re-frame-test" random-user-number "@example.com")
                :password "re-frame-test"})

(use-fixtures :each (fixtures/re-frame-state))

(deftest register
  (async done
    (reg-event-db
      :register-success
      (fn [_ [_ {:keys [user] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/user user))
        (is (= (select-keys test-user [:username :email])
               (select-keys user [:username :email])))
        (done)))
    (dispatch [:register! test-user])))

(deftest login
  (async done
    (reg-event-db
      :login-success
      (fn [_ [_ {:keys [user] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/user user))
        (is (= (select-keys test-user [:username :email])
               (select-keys user [:username :email])))
        (done)))
    (dispatch [:login! test-user])))

(deftest get-tags
  (async done
    (reg-event-db
      :get-tags-success
      (fn [_ [_ {:keys [tags] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/tags tags))
        (done)))
    (dispatch [:get-tags])))