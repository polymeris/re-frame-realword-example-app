(ns conduit.integration-test
  (:require [cljs.test :refer-macros [deftest testing is async use-fixtures]]
            [re-frame.core :as re-frame :refer [dispatch reg-event-db]]
            [cljs.spec :as spec]
            [conduit.schema :as schema]
            [conduit.events]))

(defn fixture-re-frame
  []
  (let [restore-re-frame (atom nil)]
    {:before #(reset! restore-re-frame (re-frame/make-restore-fn))
     :after  @restore-re-frame}))

(use-fixtures :each (fixture-re-frame))

(def random-user-number (rand-int 1e9))

(def test-user {:username (str "re-frame test user" random-user-number)
                :email    (str "re-frame-test" random-user-number "@example.com")
                :password "re-frame-test"})

(print test-user)

(deftest register-failure
  (async done
    (reg-event-db
      :register-failure
      (fn [_ [_ {status :status}]]
        (is (not= 200 status))
        (done)))
    (dispatch [:register! nil])))

(deftest register-success
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

(deftest login-failure
  (async done
    (reg-event-db
      :login-failure
      (fn [_ [_ {status :status}]]
        (is (not= 200 status))
        (done)))
    (dispatch [:login! nil])))

(deftest login-success
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

(deftest create-article-success
  (async done
    (reg-event-db
      :create-article-success
      (fn [_ [_ {:keys [article] :as response}]]
        (print "response was " response)
        (is (spec/valid? ::schema/article tags))
        (done)))
    (dispatch [:create-article! {:title "Test article" :}])))