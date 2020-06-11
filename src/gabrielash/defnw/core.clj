;  Copyright (C) 2020 Gabriel Ash

; This program and the accompanying materials are made available under the
; terms of the Eclipse Public License 2.0 which is available at
; http://www.eclipse.org/legal/epl-2.0 .
; This code is provided as is, without any guarantee whatsoever.

(ns gabrielash.defnw.core
  (:require [net.cgrand.seqexp :as se]))

(def args+pp+body
  "seqex match for function body declaration based on seqex documentation" 
  (se/cat
   (se/as :argv vector?)
   (se/? (se/as :pp map?))
   (se/as :body (se/* se/_))))

(def fn-def
  "seqex match for full function definition based on seqex documentation"
  (se/cat
   (se/as :name symbol?)
   (se/? (se/as :docstring string?))
   (se/? (se/as :meta map?))
   (se/|
    (se/as :single args+pp+body)
    (se/as :multiple
           (se/+ (partial se/exec
                          args+pp+body))))))


(defn- wrap-in-let
  [letv body]
  (println "####" letv body)
  (if letv
    (do  (assert (vector? letv)       "defnw -> :let value must be a vector!")
         (assert (seq letv)           "defnw -> :let vector cannot be empty!")
         (assert (even? (count letv)) "defnw -> :let requires even number of exprs!")
         (seq ['let letv body]))
    body))


(defn- wrap-in-cond
  [condv body]
  (println ";;;; " condv)
  (if condv
    (do  (assert (vector? condv)       "defnw -> :cond value must be a vector!")
         (assert (seq condv)           "defnw -> :cond vector cannot be empty!")
         (assert (even? (count condv)) "defnw -> :cond requires even number of exprs!")
         (-> condv
             (conj :else body)
             (seq)
             (conj 'cond)))
    body))


(defn- transform-body
  "apply pre-post transformations to body if given"
  [body]
  (let [match-map (se/exec args+pp+body body)
        pp (first (:pp match-map))
        argv (first (:argv match-map))
        body (cons 'do (:body match-map))]

    (do (println "<<< " match-map)
        (println ">   " pp)
        (println "[]> " argv)
        (println "()> " body))

    (if (seq pp)
      (seq [argv pp (->> body
                         (wrap-in-cond (:cond pp))
                         (wrap-in-let (:let pp)))])
      (seq [argv body]))))


(defmacro defnw
  "defn alternative that allows definition of 
     conditions and return values for special cases 
     inside the pre-post map"
  [& definition]
  (let [match-map (se/exec
                   fn-def definition)
        {:keys [name docstring meta]} match-map
        preamble (->> [name docstring meta]
                      (filter identity)
                      (map first)
                      (concat '(defn)))
        body (cond (contains? match-map :single)
                   [(:single match-map)]
                   (contains? match-map :multiple)
                   (:multiple match-map)
                   :else
                   (throw (ex-info "malformed definition: missing body"
                                   {:data match-map})))
        macroexpanded (doall
                        (concat preamble
                          (map transform-body body)))]

        (do (println "=== " name docstring meta)
            (println "**** " preamble)
            (println match-map)
            (println "<*" macroexpanded "*>"))

    macroexpanded))
