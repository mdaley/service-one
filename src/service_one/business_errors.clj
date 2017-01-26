(ns service-one.business-errors
  "Good enough business error handling in clj
  https://adambard.com/blog/acceptable-error-handling-in-clojure/
  To fall into line with Haskel and scala we are adopting the following:
  success: [nil value]
  success: [nil nil]
  failure: [value nil]")

(defn success?
  "return true if [nil anything]"
  [[err _ :as v]]
  (nil? err))

(defn on-success
  [f]
  (fn [[err value :as v] & args]
    (if (success? v)
      [nil (f value @args)]
      v)))

(defn either
  [[err value :as v] failure-fn success-fn]
  (if (success? v)
    (success-fn value)
    (failure-fn err)))

(defmacro success->
  "Threading macro for dealing with business errors where the value can be [nil anything] for success or [anything nil] for failure.
   Will only pass the value to next function if previous is not failure (only passes value not [nil value] to function)
   Return type is [failure nil] or [nil success] or [nil nil]
   Example:
   (defn do-business-stuff [v]
       [nil abc])
   (defn more-buisness-stuff [v]
       [nil result])
   (success-> {:a 324}
              (do-business-stuff 778)
              more-business-stuff)
   ;=> [nil result]"
  [expr & forms]
  (let [g (gensym)
        steps (map (fn [step] `(if (success? ~g) (-> (second ~g) ~step) ~g))
                   forms)]
    `(let [~g [nil ~expr]
           ~@(interleave (repeat g) (butlast steps))]
       ~(if (empty? steps)
          g
          (last steps)))))

(defmacro success->>
  "Threading macro for dealing with business errors where the value can be [nil anything] for success or [anything nil] for failure.
   Will only pass the value to next function if previous is not failure (only passes value not [nil value] to function)
   Return type is [failure nil] or [nil success] or [nil nil]
   Example:
   (defn do-business-stuff [v]
       [nil abc])
   (defn more-buisness-stuff [v]
       [nil result])
   (success->> {:a 324}
               (do-business-stuff 778)
               more-business-stuff)
   ;=> [nil result]"
  [expr & forms]
  (let [g (gensym)
        steps (map (fn [step] `(if (success? ~g) (->> (second ~g) ~step) ~g))
                   forms)]
    `(let [~g [nil ~expr]
           ~@(interleave (repeat g) (butlast steps))]
       ~(if (empty? steps)
          g
          (last steps)))))
