(ns conduit.views.editor
  (:require [conduit.views.components :refer [large-input textarea input]]))

(defn page []
  (let []
    (fn []
      [:div.editor-page
       [:div.container.page
        [:div.row
         [:div.col-md-10.offset-md-1.col-xs-12
          [:form
           [:fieldset
            [large-input {:placeholder "Article Title"}]
            [input {:placeholder "What's this article about?"}]
            [textarea {:placeholder "Write your article (in markdown)"}]
            [input {:placeholder "Enter tags"}]
            [:button.btn.btn-lg.pull-xs-right.btn-primary {:type :button} "Publish Article"]]]]]]])))