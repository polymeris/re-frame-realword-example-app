(ns conduit.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [conduit.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 1))))
