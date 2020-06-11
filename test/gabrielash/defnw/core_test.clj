;  Copyright (C) 2020 Gabriel Ash

; This program and the accompanying materials are made available under the
; terms of the Eclipse Public License 2.0 which is available at
; http://www.eclipse.org/legal/epl-2.0 .
; This code is provided as is, without any guarantee whatsoever.

(ns gabrielash.defnw.core-test
  (:require [clojure.test :refer :all]
            [gabrielash.defnw.core :refer :all]))


  (def p1 {:first-name "Jane" :last-name "Smith" :name-withheld? true})
  (def p2 {:first-name "Don" :last-name "Brown" :name-withheld? false})

  
(defnw
    m0
    [x y]
    {:cond [(> x 0) nil]}
    (prn x)
    (* x y))

(defnw 
    ^:big m1
    "test a single function with dostring and meta"
    [x y]
    {:cond [(> x 0) nil]}
    (prn x)
    (* x y))

(defnw 
    m2 
    "a double function with map meta"
    {:custom-meta :interesting} 
    ([x y & rs]
    (= x 7))
    ([]
     7)
    ([x y]
    {:cond [(> x 0) nil]}
    (prn x)
    (* x y)))

  ;; a double with let
(defnw 
    m3 
    {:custom-meta :boring}
    ([x y]
     {:let [z (* x y)]
      :cond [(> x y) 0 (= 0 (* x y)) -1]}
     
     (+ x y z))
  
    ([x]
     {:let [y (* x x)]}
  
     (+ x y)))
  
(defnw m4 
  "a single function in double format"
    [x y]
    {:cond [(> x 0) nil]}
    (prn x)
    (* x y))




  (defnw add-display-name
    [{:keys [ first-name last-name name-withheld? ] :as person}]

      {:pre [(string? last-name) (seq last-name)]
       :cond [name-withheld? person]}

      (assoc person
         :display-name
         (str first-name " " last-name)))



(deftest defnw-test
  (is (= 7 (m2)))
  (is (nil? (m2 7 7)))
  (is (= -49 (m2 -7 7)))
  (is (false? (m2 -7 7 5)))
  (is (true? (m2 7 7 5)))
  (is (= -70 (m1 -7 10)))
  (is (nil? (m1 7 10)))
  (is (= -70 (m0 -7 10)))
  (is (nil? (m0 7 10)))
  (is (= -70 (m4 -7 10)))
  (is (nil? (m4 7 10)))
  (is (= 110  (m3 10 )))
  (is (= 0 (m3 10 5)) )
  (is (= 65 (m3 5 10)))
  (is (= -1 (m3 0 5)))
  (is (contains? (meta #'m3) :custom-meta))
  (is (contains? (meta #'m2) :custom-meta))
  (is (contains? (meta #'m1) :big))
  ; add assertion fail tests
  (let [rval1 (add-display-name p1)
        rval2 (add-display-name p2)]
    (is (not (contains? rval1 :display-name)))
    (is (contains? rval2 :display-name))))
