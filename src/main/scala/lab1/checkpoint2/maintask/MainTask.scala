package org.marius
package lab1.checkpoint2.maintask

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object MainTask {
  def removeConsecutiveDuplicates(list: List[Int]): Unit = {
    val removedConsecutiveDuplicatesArr: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    removedConsecutiveDuplicatesArr.addOne(list(0))

    for (i <- 1 to list.length - 1 by 1) {
      if (list(i) != list(i-1)) {
        removedConsecutiveDuplicatesArr.addOne(list(i))
      }
    }

    removedConsecutiveDuplicatesArr.foreach(x => print(x + " "))
    println()
  }

  def lineWords(wordsArr: List[String]): Unit = {
    val firstRow = Set('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p')
    val secondRow = Set('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l')
    val thirdRow = Set('z', 'x', 'c', 'v', 'b', 'n', 'm')

    val filteredWords = wordsArr.filter(word => {
      val lowerCaseWord = word.toLowerCase()
      val setToCheck = if (firstRow.contains(lowerCaseWord.charAt(0))) {
          firstRow
        }
        else if (secondRow.contains(lowerCaseWord.charAt(0))) {
          secondRow
        }
        else {
          thirdRow
        }

        lowerCaseWord.forall(setToCheck.contains)
        // lowerCaseWord.forall(letter => setToCheck.contains(letter))
    })

    filteredWords.foreach(x => print(x + " "))
    println()
  }

}
