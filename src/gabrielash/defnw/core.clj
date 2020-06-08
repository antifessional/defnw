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


(defn- transform-body
  "moves body into the :else clause of the :cond defined
   in the pre-post map, if there is such a :cond"
  [body]
  (let [match-map (se/exec args+pp+body body)
        pp (first (:pp match-map))
        argv (first (:argv match-map))
        conds  (:cond pp)
        body (cons 'do (:body match-map))]

    #_(do (println "<<< " match-map)
          (println ">   " pp)
          (println ">>  " conds )
          (println "[]> " argv)
          (println "()> " body))

    (cond  conds  (seq
                    [argv
                     pp
                     (-> conds 
                         (conj :else body)
                         (seq)
                         (conj 'cond))])
           pp      (seq [argv pp body])
           :else   (seq [argv body]))))


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

        #_(do (println "=== " name docstring meta)
          (println "**** " preamble)
          (println match-map)
          (println "<*" macroexpanded "*>"))

    macroexpanded))
