(defproject gabrielash/defnw "0.2.4"
  :description
  "defnw is a defn substitute macro with special cases handling 
   moved to the pre-post map"
  :url "http://github.com/gabrielash/defnw"

  :license {:name "GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.1"] 
                [net.cgrand/seqexp "0.6.2"]]

  :plugins [
                [lein-codox "0.10.7"]
           ]      

  ;:repl-options {}

  :codox { 
      :language :clojure
      :output-path "/home/gash/docroot/codox/defnw"
      :source-uri "https://gitea.lab9.antifessional.net:3000/dev/defnw/src/commit/{git-commit}/src/{classpath}"
      :metadata {:doc/format :markdown}
      :html {
            :namespace-list :flat
            :transforms
              [[:body [:div.sidebar.primary ]]
               [:prepend 
                  [:h3 
                    [:a {:href "/"} "All Packages"]]]]}
  }


)

