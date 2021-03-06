(ns conduit.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [json-request-format json-response-format]]
            [clojure.string :as string]
            [cljs-time.format :as f]
            [akiroz.re-frame.storage :as storage]))

(def base-url "https://conduit.productionready.io")

(defn uri [& path]
  (string/join "/" (concat [base-url "api"] path)))

(defn authorization-headers [db]
  [:Authorization (str "Token " (get-in db [:user :token]))])

(defn reg-event-db-storing-user
  [event-id handler]
  (reg-event-fx
    event-id
    [(storage/persist-db :conduit :user)]
    (fn [{:keys [db]} event-vec]
      {:db (handler db event-vec)})))

(reg-event-db-storing-user
  :initialize-db
  (fn [db _]
    (or db {})))

(reg-event-db
  :set-active-page
  (fn [db [_ active-panel]]
    (assoc db :active-page active-panel)))

(reg-event-db
  :set-active-article
  (fn [db [_ active-article]]
    (assoc db :active-article active-article)))

(reg-event-db
  :set-article-tag-filter
  (fn [db [_ tag]]
    (assoc-in db [:article-filters :tags] [tag])))

(reg-event-db
  :add-article-tag-filter
  (fn [db [_ tag]]
    (update-in db [:article-filters :tags] conj tag)))

(reg-event-db
  :clear-article-tag-filter
  (fn [db [_ tag]]
    (assoc-in db [:article-filters :tags] [])))

(reg-event-db
  :set-active-profile
  (fn [db [_ active-profile]]
    (assoc db :active-profile active-profile)))

(reg-event-db
  :api-request-failure
  (fn [db [_ & q]]
    (let [request (butlast q)
          response (last q)]
      (assoc-in db
                (into [:pending-requests] request)
                [:failed (or (get-in response [:response :errors])
                             {:error [(get response :status-text)]})]))))

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
                  :on-failure      [:api-request-failure :login!]}}))

(reg-event-db-storing-user
  :login-success
  (fn [db [_ {user :user}]]
    (dispatch [:set-active-page :main])
    (dispatch [:get-user])
    (-> db
        (assoc-in [:pending-requests :login!] false)
        (assoc :user user))))


(reg-event-db-storing-user
  :logout!
  (fn [db _]
    (dispatch [:set-active-page :main])
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
                  :on-failure      [:api-request-failure :register!]}}))

(reg-event-db
  :register-success
  (fn [db [_ {user :user}]]
    (dispatch [:set-active-page :main])
    (-> db
        (assoc-in [:pending-requests :register!] false)
        (assoc :user user))))

(reg-event-fx
  :get-user
  (fn [{:keys [db]} _]
    {:http-xhrio {:method          :get
                  :uri             (uri "user")
                  :headers         (authorization-headers db)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-user-success]
                  :on-failure      [:api-request-failure :get-user]}}))

(reg-event-db
  :get-user-success
  (fn [db [_ {user :user}]]
    (assoc db :user user)))

(reg-event-fx
  :update-user!
  (fn [{:keys [db]} [_ user]]
    {:db         (assoc-in db [:pending-requests :update-user!] :pending)
     :http-xhrio {:method          :put
                  :uri             (uri "user")
                  :headers         (authorization-headers db)
                  :params          {:user user}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:update-user-success]
                  :on-failure      [:api-request-failure :update-user!]}}))

(reg-event-db
  :update-user-success
  (fn [db [_ {user :user}]]
    (-> db
        (assoc-in [:pending-requests :update-user!] false)
        (assoc :user user))))

(reg-event-fx
  :update-active-profile
  (fn [{:keys [db]} _]
    {:dispatch [:get-profile (:active-profile db)]}))

(reg-event-fx
  :get-profile
  (fn [{:keys [db]} [_ username]]
    {:http-xhrio {:method          :get
                  :uri             (uri "profiles" username)
                  :headers         (when (:user db) (authorization-headers db))
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-profile-success username]
                  :on-failure      [:api-request-failure :get-profile username]}}))

(reg-event-db
  :get-profile-success
  (fn [db [_ username {profile :profile}]]
    (assoc-in db [:profiles username] profile)))

(reg-event-fx
  :follow-profile!
  (fn [{:keys [db]} [_ username]]
    {:db         (assoc-in db [:pending-requests :follow-profile! username] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "profiles" username "follow")
                  :headers         (authorization-headers db)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:follow-profile-success username]
                  :on-failure      [:api-request-failure :follow-profile! username]}}))

(reg-event-db
  :follow-profile-success
  (fn [db [_ username {profile :profile}]]
    (-> db
        (assoc-in [:pending-requests :follow-profile! username] false)
        (assoc-in [:profiles username] profile))))

(reg-event-fx
  :unfollow-profile!
  (fn [{:keys [db]} [_ username]]
    {:db         (assoc-in db [:pending-requests :unfollow-profile! username] :pending)
     :http-xhrio {:method          :delete
                  :uri             (uri "profiles" username "follow")
                  :headers         (authorization-headers db)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:unfollow-profile-success username]
                  :on-failure      [:api-request-failure :unfollow-profile! username]}}))

(reg-event-db
  :unfollow-profile-success
  (fn [db [_ username {profile :profile}]]
    (-> db
        (assoc-in [:pending-requests :unfollow-profile! username] false)
        (assoc-in [:profiles username] profile))))

(reg-event-fx
  :get-articles
  (fn [{:keys [db]} [_ params]]
    {:db         (assoc-in db [:pending-requests :get-articles] :pending)
     :http-xhrio {:method          :get
                  :uri             (uri "articles")
                  :headers         (when (:user db) (authorization-headers db))
                  :params          params
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-articles-success]
                  :on-failure      [:api-request-failure :get-articles]}}))

(reg-event-db
  :get-articles-success
  (fn [db [_ {articles :articles}]]
    (-> db
        (assoc-in [:pending-requests :get-articles] false)
        (update :articles #(merge % (into {} (map (juxt :slug identity) articles)))))))

(reg-event-fx
  :create-article!
  (fn [{:keys [db]} [_ article]]
    {:db         (assoc-in db [:pending-requests :create-article!] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "articles")
                  :headers         (authorization-headers db)
                  :params          {:article article}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:create-article-success article]
                  :on-failure      [:api-request-failure :create-article!]}}))

(reg-event-db
  :create-article-success
  (fn [db [_ _]]
    (assoc-in db [:pending-requests :create-article!] false)))

(reg-event-fx
  :update-article!
  (fn [{:keys [db]} [_ article]]
    {:db         (assoc-in db [:pending-requests :update-article! article] :pending)
     :http-xhrio {:method          :put
                  :uri             (uri "articles" (:slug article))
                  :headers         (authorization-headers db)
                  :params          {:article article}
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:update-article-success article]
                  :on-failure      [:api-request-failure :update-article! article]}}))

(reg-event-db
  :update-article-success
  (fn [db [_ article _]]
    (assoc-in db [:pending-requests :update-article! article] false)))

(reg-event-fx
  :delete-article!
  (fn [{:keys [db]} [_ slug]]
    {:db         (assoc-in db [:pending-requests :delete-article! slug] :pending)
     :http-xhrio {:method          :delete
                  :uri             (uri "articles" slug)
                  :headers         (authorization-headers db)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:delete-article-success slug]
                  :on-failure      [:api-request-failure :delete-article! slug]}}))

(reg-event-db
  :delete-article-success
  (fn [db [_ article _]]
    (assoc-in db [:pending-requests :delete-article! article] false)))

(reg-event-fx
  :favorite-article!
  (fn [{:keys [db]} [_ slug]]
    {:db         (assoc-in db [:pending-requests :favorite-article! slug] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "articles" slug "favorite")
                  :headers         (authorization-headers db)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:favorite-article-success slug]
                  :on-failure      [:api-request-failure :favorite-article! slug]}}))

(reg-event-db
  :favorite-article-success
  (fn [db [_ slug {article :article}]]
    (-> db
        (assoc-in [:articles slug] article)
        (assoc-in [:pending-requests :favorite-article! slug] false))))

(reg-event-fx
  :unfavorite-article!
  (fn [{:keys [db]} [_ slug]]
    {:db         (assoc-in db [:pending-requests :unfavorite-article! slug] :pending)
     :http-xhrio {:method          :delete
                  :uri             (uri "articles" slug "favorite")
                  :headers         (authorization-headers db)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:unfavorite-article-success slug]
                  :on-failure      [:api-request-failure :unfavorite-article! slug]}}))

(reg-event-db
  :unfavorite-article-success
  (fn [db [_ slug {article :article}]]
    (-> db
        (assoc-in [:articles slug] article)
        (assoc-in [:pending-requests :unfavorite-article! slug] false))))

(reg-event-fx
  :post-comment!
  (fn [{:keys [db]} [_ article comment]]
    {:db         (assoc-in db [:pending-requests :post-comment! article] :pending)
     :http-xhrio {:method          :post
                  :uri             (uri "articles" (:slug article) "comments")
                  :headers         (authorization-headers db)
                  :params          {:comment comment}
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:post-comment-success article comment]
                  :on-failure      [:api-request-failure :post-comment! article comment]}}))

(reg-event-fx
  :get-comments
  (fn [{:keys [db]} [_ {slug :slug}]]
    {:db         (assoc-in db [:pending-requests :get-comments slug] :pending)
     :http-xhrio {:method          :get
                  :uri             (uri "articles" slug "comments")
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-comments-success slug]
                  :on-failure      [:api-request-failure :get-comments slug]}}))

(reg-event-db
  :get-comments-success
  (fn [db [_ slug {comments :comments}]]
    (-> db
        (assoc-in [:pending-requests :get-comments slug] false)
        (assoc-in [:articles slug :comments] comments))))

(reg-event-fx
  :delete-comment!
  (fn [{:keys [db]} [_ article comment]]
    {:db         (assoc-in db [:pending-requests :delete-comment! article] :pending)
     :http-xhrio {:method          :delete
                  :uri             (uri "articles" (:slug article) "comments" (:id comment))
                  :headers         (authorization-headers db)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:delete-comment-success article comment]
                  :on-failure      [:api-request-failure :delete-comment! article comment]}}))

(reg-event-fx
  :get-tags
  (fn [{:keys [db]} _]
    {:db         (assoc-in db [:pending-requests :get-tags] :pending)
     :http-xhrio {:method          :get
                  :uri             (uri "tags")
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:get-tags-success]
                  :on-failure      [:api-request-failure :get-tags]}}))

(reg-event-db
  :get-tags-success
  (fn [db [_ {tags :tags}]]
    (-> db
        (assoc-in [:pending-requests :get-tags] false)
        (assoc :tags tags))))
