package constraints

import cafesat.api.FormulaBuilder._
import cafesat.api.Formulas._
import cafesat.api.Solver._

import scala.annotation.tailrec

/**
  * This object contains utility functions for encoding
  * some arithmetic constraints as boolean ones
  */
object Arithmetic {

  /**
   * Transforms a positive integer in binary form into its integer representation.
   * The `head` element of the input list contains the most
   * significant bit (the list is in big-endian form).
   */
  def binary2int(n: List[Boolean]): Int = {
    def binaryHelper(n: List[Boolean], i: Int): Int = {
      if (n == Nil) i
      else if (n.head == true) binaryHelper(n.tail, i + scala.math.pow(2,(n.size - 1)).toInt)
      else binaryHelper(n.tail, i)
    }  
    binaryHelper(n, 0)
  }

  /**
   * Encodes a positive integer number into base 2.
   * The `head` element of the resulting list contains the most significant
   * bit. This function should not return unnecessary leading zeros.
   */
  def int2binary(n: Int): List[Boolean] = {
    def intHelper(n: Int, myList: List[Boolean]): List[Boolean] = {
      if (n == 0) myList
      else intHelper(n / 2, (n % 2 == 1) :: myList)
    }
    if (n == 0) List(false)
    else intHelper(n, Nil)
  }
    

  /**
   * This function takes two arguments, both representing positive
   * integers encoded in binary as lists of propositional formulas
   * (true for 1, false for 0). It returns
   * a formula that represents a boolean circuit that constraints
   * `n1` to be less than or equal to `n2`
   */
  def lessEquals(n1: List[Formula], n2: List[Formula]): Formula = {
    def lessEqualHelper(myList: List[(Formula, Formula)]): Formula = {
      myList match {
        case Nil => True
        case(a, b) :: tail => (lessEqualHelper(tail) && !(a xor b)) || (!a && b)
      }
    }
    lessEqualHelper(n1.reverse.zipAll(n2.reverse, False, False).reverse)
  }

  /**
   * A full adder is a circuit that takes 3 one bit numbers, and returns the
   * result encoded over two bits: (cOut, s)
   */
  def fullAdder(a: Formula, b: Formula, cIn: Formula): (Formula, Formula) = ((a && b) || (cIn && (a xor b)), a xor b xor cIn)

  /**
   * This function takes two arguments, both representing positive integers
   * encoded as lists of propositional variables. It returns a pair.
   *
   * The first element of the pair is a `List[Formula]`, and it represents
   * the resulting binary number.
   * The second element is a set of intermediate constraints that are created
   * along the way.
   *
   */
  def adder(n1: List[Formula], n2: List[Formula]): (List[Formula], Set[Formula]) = {
    def adderHelper(myList: List[(Formula, Formula)], cIn0: Formula, acc: (List[Formula], Set[Formula])): (List[Formula], 
        Set[Formula]) = { 
      myList match {
        case Nil => (cIn0 :: acc._1, acc._2)
        case(a, b) :: tail => {
          val carry = fullAdder(myList.head._1, myList.head._2, cIn0)
          val newC = propVar()
          val newS = propVar()
          adderHelper(tail, newC, (newS :: acc._1, acc._2 + (newC iff carry._1) + (newS iff carry._2)))
        }
      }
    }
    adderHelper(n1.reverse.zipAll(n2.reverse, False, False), False, (List(), Set()))
  }

  /**
   * A helper function that creates a less-equals formula
   * taking an integer and a formula as parameters
   */
  def lessEqualsConst(cst: Int, n: List[Formula]): Formula = {
    lessEquals(int2binary(cst), n)
  }

  /**
   * A helper function that creates a less-equals formula
   * taking a formula and an integer as parameters
   */
  def lessEqualsConst(n: List[Formula], cst: Int): Formula = {
    lessEquals(n, int2binary(cst))
  }


}
