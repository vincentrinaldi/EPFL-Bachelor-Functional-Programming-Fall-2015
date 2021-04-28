package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  lazy val genHeap: Gen[H] = for {
    a <- arbitrary[Int]
    h <- oneOf(const(empty), genHeap)
  } yield insert(a, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)
  
  property("gen1") = forAll { h: H =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }
  
  property("theMinBetweenTwoElems") = forAll { (a: Int, b: Int) =>
    val h = insert(b, insert(a, empty))
    val theSmallest = if (a < b) a else b
    findMin(h) == theSmallest
  }
  
  property("emptyHeapByDeletingTheMin") = forAll { a: Int =>
    val h = deleteMin(insert(a, empty))
    h == empty
  }
  
  property("gettingSortedSequence") = forAll { (h: H) =>
    def sortingHelper(myHeap: H, myList: List[Int]): List[Int] = {
      if (isEmpty(myHeap)) myList
      else findMin(myHeap) :: sortingHelper(deleteMin(myHeap), myList)
    }
    val theSortedList = sortingHelper(h, Nil)
    theSortedList == theSortedList.sorted
  }
  
  property("gettingTheMinOfOneOrTheOther") = forAll { (h1: H, h2: H) =>
    val theMeldedHeap = meld(h1, h2)
    findMin(theMeldedHeap) == findMin(h1) || findMin(theMeldedHeap) == findMin(h2)
  }
  
  property("gettingSameMeldingWithElemTransfert") = forAll { (h1: H, h2: H) =>
    def sortingHelper(myHeap: H, myList: List[Int]): List[Int] = {
      if (isEmpty(myHeap)) myList
      else findMin(myHeap) :: sortingHelper(deleteMin(myHeap), myList)
    }
    val theMainMeldingHeap = meld(h1, h2)
    val theMainSortedList = sortingHelper(theMainMeldingHeap, Nil)
    val theOtherMeldingHeap = meld(deleteMin(h1), insert(findMin(h1), h2))
    val theOtherSortedList = sortingHelper(theOtherMeldingHeap, Nil)
    theMainSortedList == theOtherSortedList
  }
}
