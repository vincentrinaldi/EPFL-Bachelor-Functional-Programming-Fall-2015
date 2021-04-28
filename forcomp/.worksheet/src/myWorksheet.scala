import forcomp.Anagrams._

object myWorksheet {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(91); 
  println("Welcome to the Scala worksheet")
  type Word = String;$skip(42); val res$0 = 
  Word w = "Welcome";System.out.println("""res0: <error> = """ + $show(res$0));$skip(26); 
  val a = List("Welcome");System.out.println("""a  : List[String] = """ + $show(a ));$skip(29); 
  val b = wordOccurrences(a);System.out.println("""b  : <error> = """ + $show(b ))}
}
