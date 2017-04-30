(ns conduit.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [conduit.events-test]))

(doo-tests 'conduit.events-test)
