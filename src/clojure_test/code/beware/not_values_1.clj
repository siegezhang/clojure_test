;---
; Excerpted from "Mastering Clojure Macros",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/cjclojure for more book information.
;---
 (defn square [x] (* x x))
;=> #'user/square
 (map square (range 10))
;=> (0 1 4 9 16 25 36 49 64 81)
 (defmacro square [x] `(* ~x ~x))
;=> #'user/square
 (map square (range 10))
;CompilerException java.lang.RuntimeException:
;  Can't take value of a macro: #'user/square, compiling: (NO_SOURCE_PATH:1:1)
