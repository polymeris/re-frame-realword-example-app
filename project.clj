(defproject conduit "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.0"]
                 [re-frame "0.9.2"]
                 [re-frisk "0.3.2"]
                 [secretary "1.2.3"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [prismatic/dommy "1.1.0"]
                 [cljsjs/react-select "1.0.0-rc.1"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [clojure-humanize "0.2.2"]
                 [markdown-clj "0.9.99"]
                 [akiroz.re-frame/storage "0.1.1"]]
  :plugins [[lein-cljsbuild "1.1.4"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "test/js"]
  :figwheel {:css-dirs ["resources/public/css"]}
  :profiles {:dev
             {:dependencies [[binaryage/devtools "0.8.2"]
                             [com.cemerick/piggieback "0.2.1"]
                             [figwheel-sidecar "0.5.9"]]
              :source-paths ["src/cljs" "dev"]
              :plugins      [[lein-figwheel "0.5.9"]
                             [lein-doo "0.1.7"]]}}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :aliases {"test" ["with-profile" "test" "doo" "firefox" "test" "once"]}
  :cljsbuild
  {:builds [{:id           "dev"
             :source-paths ["src/cljs"]
             :figwheel     {:on-jsload "conduit.core/mount-root"}
             :compiler     {:main                 conduit.core
                            :output-to            "resources/public/js/compiled/app.js"
                            :output-dir           "resources/public/js/compiled/out"
                            :asset-path           "js/compiled/out"
                            :source-map-timestamp true
                            :preloads             [devtools.preload]
                            :external-config      {:devtools/config {:features-to-install :all}}}}
            {:id           "min"
             :source-paths ["src/cljs"]
             :compiler     {:main            conduit.core
                            :output-to       "resources/public/js/compiled/app.js"
                            :optimizations   :advanced
                            :closure-defines {goog.DEBUG false}
                            :pretty-print    false}}
            {:id           "test"
             :source-paths ["src/cljs" "test/cljs"]
             :compiler     {:main          conduit.runner
                            :output-to     "resources/public/js/compiled/test.js"
                            :output-dir    "resources/public/js/compiled/test/out"
                            :optimizations :none}}]})
