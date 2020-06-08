# defnw

A Clojure library that defines `defnw`, a macro that replaces 
defn so as to allow definition of special cases in the pre-post map.

## Rational 

A common pattern is to test arguments for certain conditions that are incompatible 
with the execution of the function's main purpose. 

```
    (defn do-that 
      [ arg-1 arg-2 ]
        (cond (that-is-inapropriate? arg-1)
              special-case-return-value
              (that-is-impossible-with? arg-2)
              special-case-return-value-2
              :else 
              (actually-do-that arg-1 arg-2))
```

defnw increases readability by allowing to limit the body of the function to
the main purpose of the code (the :else clause), moving the argument testing
to the pre-post map.

Unlike conditions in the :pre vector of the pre-post map, conditions in the :cond
vecotr do not generate assert failures. Each condition must be followed by an
expression returned when the condition is true.


## Usage

```

  (defnw add-display-name
    [{:keys [ first-name last-name name-withwheld? ] :as person}]

      {:pre [(string? last-name)]

       :cond [name-withheld? person]}

      (assoc person
             :display-name
             (str first-name " " last-name)))

```

### Caveats

 * body is wrapped in `do`
 * if you're redefining `cond` or `do`, well ...
 * This is a learning exercise, use at your own risk

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

This code is provided as is, without any guarantee whatsoever.