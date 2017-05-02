(ns conduit.runner
  (:require [cljs.test :refer-macros [use-fixtures]]
            [doo.runner :refer-macros [doo-tests]]
            [conduit.integration-test]
            [conduit.authenticated-integration-test]))

(doo-tests 'conduit.integration-test
           'conduit.authenticated-integration-test)
