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

(deftest get-profile
  (async done
    (reg-event-db
      :get-profile-success
      (fn [_ [_ _ {:keys [profile] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/profile profile))
        (done)))
    (dispatch [:get-profile (:username test-user)])))

(deftest get-articles-simple
  (async done
    (reg-event-db
      :get-articles-success
      (fn [_ [_ {:keys [articles] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/articles articles))
        (done)))
    (dispatch [:get-articles {:limit 3}])))

(deftest get-articles-by-tag
  (async done
    (reg-event-db
      :get-articles-success
      (fn [_ [_ {:keys [articles] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/articles articles))
        (done)))
    (dispatch [:get-articles {:tag "test-tag" :limit 3}])))

(deftest get-articles-by-author
  (async done
    (reg-event-db
      :get-articles-success
      (fn [_ [_ {:keys [articles] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/articles articles))
        (done)))
    (dispatch [:get-articles {:author "re-frame Test User" :limit 3}])))

(deftest get-articles-by-favorited
  (async done
    (reg-event-db
      :get-articles-success
      (fn [_ [_ {:keys [articles] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/articles articles))
        (done)))
    (dispatch [:get-articles {:favorited "re-frame Test User" :limit 3}])))