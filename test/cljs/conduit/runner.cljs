(ns conduit.runner
  (:require [cljs.test :refer-macros [use-fixtures]]
            [doo.runner :refer-macros [doo-tests]]
            [conduit.events-test]))

(doo-tests 'conduit.events-test)
