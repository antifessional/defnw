 ;  Copyright (C) 2020 Gabriel Ash

 ; This program and the accompanying materials are made available under the
 ; terms of the Eclipse Public License 2.0 which is available at
 ; http://www.eclipse.org/legal/epl-2.0 .

(ns gabrielash.defnw.core-test
  (:require [clojure.test :refer :all]
            [gabrielash.defnw.core :refer :all]))


(defnw m1
  "doc"
  [x y]
  {:cond [(> x 0) nil]}
  (prn x)
  (* x y))

(defnw m2 "doc"
  ([x y & rs]
   (= x 7))
  ([]
   7)
  ([x y]
   {:cond [(> x 0) nil]}
   (prn x)
   (* x y)))



(defnw add-display-name
  [{:keys [first-name last-name age] :as person}]

  {:pre [(string? last-name) 
         (int? age)
         (seq last-name)]
   :cond [(< age 18) person]}

  (assoc person
         :display-name
         (str first-name " " last-name)))


(def p1 {:first-name "Jane" :last-name "Smith" :age 15})
(def p2 {:first-name "Don" :last-name "Brown" :age 25})


(deftest defnw-test
  (is (= 7 (m2)))
  (is (nil? (m2 7 7)))
  (is (= -49 (m2 -7 7)))
  (is (false? (m2 -7 7 5)))
  (is (true? (m2 7 7 5)))
  (is (= -70 (m1 -7 10)))
  (is (nil? (m1 7 10)))
  (let [p1 (add-display-name p1)
        p2 (add-display-name p2)]
    (is (not (contains? p1 :display-name)))
    (is (contains? p2 :display-name))))
