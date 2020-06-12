;  Copyright (C) 2020 Gabriel Ash

; This program and the accompanying materials are made available under the
; terms of the Eclipse Public License 2.0 which is available at
; http://www.eclipse.org/legal/epl-2.0 .
; This code is provided as is, without any guarantee whatsoever.

(ns gabrielash.misc.shorts-test
  (:require [clojure.test :refer :all]
            [gabrielash.misc.shorts :refer :all]))

(deftest !=_test
  (is (true? (!= 1 0)))
  (is (false? (!= 7 7)))
  (is (false? (!= 1)))
  (is (true? (!= 7 7 5)))
  (is (false? (!= 7 7 7)))
  (is (true? (!= (+ 4 4) (* 2 3))))
  (is (false? (!= (+ 4 4) (* 2 4)))))

(deftest !!_test
  (is (= (!! odd? 7) (not (odd? 7))))
  (is (= (!! contains? #{1 2 3} 4)
         (not (contains? #{1 2 3} 4))))
  (is (= (!! contains? #{1 2 3} 3)
         (not (contains? #{1 2 3} 3)))))