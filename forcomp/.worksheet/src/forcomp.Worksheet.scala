package forcomp

object Worksheet {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(79); 
  println("Welcome to the Scala worksheet");$skip(54); 
  
  val l = List("Bonjour", "je", "suis", "Vincent");System.out.println("""l  : List[String] = """ + $show(l ));$skip(28); val res$0 = 
  l.map(wordOccurrences(_));System.out.println("""res0: List[Nothing] = """ + $show(res$0))}
}
