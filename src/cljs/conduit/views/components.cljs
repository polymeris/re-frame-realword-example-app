(ns conduit.views.components
  (:require [reagent.core :as reagent]))

(defn large-textarea
  [attrs]
  [:fieldset.form-group
   [:textarea.form-control.form-control-lg (merge {:rows 8} attrs)]])

(defn textarea
  [attrs]
  [:fieldset.form-group
   [:textarea.form-control (merge {:rows 8} attrs)]])

(defn large-input
  [attrs]
  [:fieldset.form-group
   [:input.form-control.form-control-lg (merge {:type :text} attrs)]])

(defn input
  [attrs]
  [:fieldset.form-group
   [:input.form-control (merge {:type :text} attrs)]])

(defn error-list
  [errors]
  (when errors
    [:ul.error-messages
     (map (fn [[k v]] [:li {:key k} (str (name k) " " (first v))]) errors)]))

; infinite scrolling based on code by Nicolás Berger
; https://gist.github.com/nberger/b5e316a43ffc3b7d5e084b228bd83899

(defn- get-scroll-top []
  (if (exists? (.-pageYOffset js/window))
    (.-pageYOffset js/window)
    (.-scrollTop (or (.-documentElement js/document)
                     (.-parentNode (.-body js/document))
                     (.-body js/document)))))

(defn- get-el-top-position [node]
  (if (not node)
    0
    (+ (.-offsetTop node) (get-el-top-position (.-offsetParent node)))))

(defn- safe-component-mounted? [component]
  (try (boolean (reagent/dom-node component)) (catch js/Object _ false)))

(defn debounce
  "Returns a function that will call f only after threshold has passed without new calls
  to the function. Calls prep-fn on the args in a sync way, which can be used for things like
  calling .persist on the event object to be able to access the event attributes in f"
  ([threshold f] (debounce threshold f (constantly nil)))
  ([threshold f prep-fn]
   (let [t (atom nil)]
     (fn [& args]
       (when @t (js/clearTimeout @t))
       (apply prep-fn args)
       (reset! t (js/setTimeout #(do
                                   (reset! t nil)
                                   (apply f args))
                                threshold))))))

(defn infinite-scroll [props]
  ;; props is a map with :can-show-more? & :load-fn keys
  (let [listener-fn (atom nil)
        detach-scroll-listener (fn []
                                 (when @listener-fn
                                   (.removeEventListener js/window "scroll" @listener-fn)
                                   (.removeEventListener js/window "resize" @listener-fn)
                                   (reset! listener-fn nil)))
        should-load-more? (fn [this]
                            (let [node (reagent/dom-node this)
                                  scroll-top (get-scroll-top)
                                  my-top (get-el-top-position node)
                                  threshold 50]
                              (< (- (+ my-top (.-offsetHeight node))
                                    scroll-top
                                    (.-innerHeight js/window))
                                 threshold)))
        scroll-listener (fn [this]
                          (when (safe-component-mounted? this)
                            (let [{:keys [load-fn]} (reagent/props this)]
                              (when (should-load-more? this)
                                (detach-scroll-listener)
                                (load-fn)))))
        debounced-scroll-listener (debounce 200 scroll-listener)
        attach-scroll-listener (fn [this]
                                 (when-not @listener-fn
                                   (reset! listener-fn (partial debounced-scroll-listener this))
                                   (.addEventListener js/window "scroll" @listener-fn)
                                   (.addEventListener js/window "resize" @listener-fn)))]
    (reagent/create-class
      {:component-did-mount    attach-scroll-listener
       :component-did-update   attach-scroll-listener
       :component-will-unmount detach-scroll-listener
       :reagent-render         (fn [] [:div.infinite-scroll])})))