(ns conduit.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [conduit.core-test]))

(doo-tests 'conduit.core-test)
