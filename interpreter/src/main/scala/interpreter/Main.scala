package interpreter

object Main extends App {
  import java.io.{BufferedReader, InputStreamReader}
  import Lisp._
  val in = new BufferedReader(new InputStreamReader(System.in))
  // TODO: Insert code for the REPL
  var string: String = ""
  while(!string.equals("quit")){
    print("lisp> ")
    string = in.readLine()
    println(lisp2string(evaluate(string)))
  }
}

object LispCode {
  // TODO: implement the function `reverse` in Lisp.
  // From a list (a, b, c, d) it should compute (d, c, b, a)
  // Write it as a String, and test it in your REPL
  
  val reverse = """
  def (reverse L) (
    def (reverseHelper Lbis acc) (          
      if (null? Lbis) acc
      (reverseHelper (cdr Lbis) (cons (car Lbis) acc))
    )
    (reverseHelper L nil)     
  )
  """

  // TODO: implement the function `differences` in Lisp.
  // From a list (a, b, c, d ...) it should compute (a, b-a, c-b, d-c ...)
  // You might find useful to define an inner loop def
  val differences = """
  def (differences L) (
    def (differencesHelper Lbis acc) (
      if (null? (cdr Lbis)) (reverse acc)
      (differencesHelper (cdr Lbis) (cons (- (car (cdr Lbis)) (car Lbis)) acc))
    )
    (differencesHelper (cons 0 L) nil)
  )
  """
  val rebuildList = """
  def (rebuildList L) (
    if (null? L) nil
    (
    if (null? (cdr L)) L
    (cons (car L) (rebuildList (cons (+ (car L) (car (cdr L))) (cdr (cdr L)))))
    )
  )
  """

  val withDifferences: String => String =
    (code: String) => "(" + reverse + " (" + differences + " (" + rebuildList + " " + code + ")))"
}