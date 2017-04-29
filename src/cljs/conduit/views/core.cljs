(ns conduit.views.core
  (:require [re-frame.core :as re-frame]
            [conduit.views.header :refer [navbar]]
            [conduit.views.footer :refer [footer]]
            [conduit.views.article :as article]
            [conduit.views.auth :as auth]
            [conduit.views.editor :as editor]
            [conduit.views.home :as home]
            [conduit.views.profile :as profile]
            [conduit.views.settings :as settings]))

(defn page [page-name]
  (case page-name
    :home [home/page]
    :auth [auth/page]
    :settings [settings/page]
    :editor [editor/page]
    :article [article/page]
    :profile [profile/page]
    [home/page]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-page])]
    (fn []
      [:div
       [navbar]
       [page @active-panel]
       [footer]])))
