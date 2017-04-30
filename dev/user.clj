 (ns user
   (:use [figwheel-sidecar.repl-api :as ra]))

 (defn go! []
   (ra/start-figwheel!)
   (ra/cljs-repl "dev"))