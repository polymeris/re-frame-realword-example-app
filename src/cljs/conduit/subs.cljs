(ns conduit.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]
            [cljs-time.core :as t]
            [cljs-time.coerce :as c]
            [cljs-time.format :as f]
            [clojure.contrib.humanize :as humanize]))

(reg-sub
  :active-page
  (fn [db _]
    (:active-page db)))

(reg-sub
  :request-errors
  (fn [db [_ & query]]
    (let [status (get-in db (into [:pending-requests] query))]
      (when (and (vector? status)
                 (= :failed (first status)))
        (second status)))))

(reg-sub
  :request-pending
  (fn [db [_ & query]]
    (= :pending (get-in db (into [:pending-requests] query)))))

(reg-sub
  :any-request-pending
  (fn [db [_]]
    (->> (get db :pending-requests)
         (tree-seq map? vals)
         (some #(= % :pending)))))

(reg-sub
  :user
  (fn [db _]
    (:user db)))

(reg-sub
  :logged-in?
  (fn [db _]
    (some? (:user db))))

(reg-sub
  :tags
  (fn [db _]
    (:tags db)))

(defn humanize-article [{:keys [createdAt] :as article}]
  (assoc article
    :date
    (if (t/before? createdAt (t/minus (t/now) (t/days 1)))
      (f/unparse (f/formatter "EEE, dd MMM YYYY") createdAt)
      (humanize/datetime createdAt))))

(defn filter-articles [params articles]
  articles)

(reg-sub
  :articles
  (fn [db _]
    (->> (get-in db [:articles])
         (filter-articles (:article-filters db))
         (map #(humanize-article (second %)))
         (map #(dissoc % :comments)))))

(reg-sub
  :active-article
  (fn [db _]
    (->> (get-in db [:articles (:active-article db)])
         (humanize-article))))

(reg-sub
  :active-profile
  (fn [db _]
    (or (get-in db [:profiles (:active-profile db)])
        {:username (:active-profile db)})))

(reg-sub
  :popular-tags
  (fn [db _]
    (->> (get-in db [:articles nil])
         (map :tagList)
         (flatten)
         (frequencies)
         (sort-by second)
         (map first)
         (reverse)
         (take 16)
         (concat (:tags db))
         (set))))