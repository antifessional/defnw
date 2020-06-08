# defnw

A Clojure library that derfines defnw, a macro that replaces 
defn so as to allow definition of special cases in the pre-post map.

## Rational 

A common pattern is to test arguments for certain conditions that are incompatible 
with the execution of the function's main purpose. 

```
    (defn do-that 
      [ arg-1 arg-2 ]
        (cond (that-is-inapropriate? arg-1)
              proper-return-value
              (that-is-impossible-with? arg-2)
              proper-return-value-2

              :else 
              (actually-do-that arg-1 arg-2))
```

defnw increases readability by allowing to limit the body of the function to
the main purpose of the code (the :else clause), moving the argument testing
to the pre-post map.


## Usage

```

  (defnw add-display-name
    [{:keys [first-name last-name age] :as person}]

      {:pre [(string? last-name) 
         (int? age)
         (seq last-name)]
       :cond [(< age 18) person]}


      (assoc person
         :display-name
         (str first-name " " last-name)))

```


## License

Copyright © 2020 Gabriel Ash

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
