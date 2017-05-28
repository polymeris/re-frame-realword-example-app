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
  (let [active-panel (re-frame/subscribe [:active-page])]
    (fn []
      [:div
       [header/navbar]
       [page @active-panel]
       [footer/footer]])))
