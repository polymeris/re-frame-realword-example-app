(ns conduit.views.core
  (:require [re-frame.core :as re-frame]
            [conduit.views.header :as header ]
            [conduit.views.footer :as footer]
            [conduit.views.article :as article]
            [conduit.views.auth :as auth]
            [conduit.views.signup :as signup]
            [conduit.views.editor :as editor]
            [conduit.views.home :as home]
            [conduit.views.profile :as profile]
            [conduit.views.settings :as settings]))

(defn page [page-name]
  (case page-name
    :home [home/page]
    :auth [auth/page]
    :signup [signup/page]
    :settings [settings/page]
    :editor [editor/page]
    :article [article/page]
    :profile [profile/page]
    [home/page]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-page])
        requests-pending (re-frame/subscribe [:pending-requests])
        any-request-pending (re-frame/subscribe [:any-request-pending])]
    (fn []
      [:div
       [header/navbar]
       (when @any-request-pending [:div.loading-bar [:div.loading-progress]])
       [page @active-panel]
       [footer/footer]])))
