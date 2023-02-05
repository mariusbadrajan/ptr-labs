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

  def encode(word: String, shift: Int): Unit = {
    var shiftedWord = ""

    word.foreach(letter => {
      val offset = if (letter.isUpper) 'A'.toInt else 'a'.toInt
      val newLetter = ((letter.toInt + shift - offset) % 26 + offset).asInstanceOf[Char]
      shiftedWord += newLetter
    })

    println(shiftedWord)
  }

  def decode(word: String, shift: Int): Unit = {
    encode(word, 26 - shift)
  }

  def groupAnagrams(anagramsList: List[String]): Unit = {
    var anagramsMap: Map[String, ArrayBuffer[String]] = Map[String, ArrayBuffer[String]]()

    anagramsList.foreach(item => {
      val key = item.toCharArray.sortWith(_ < _).mkString
      if (!anagramsMap.contains(key)) {
        anagramsMap += (key -> ArrayBuffer(item))
      }
      else {
        anagramsMap.get(key) match {
          case Some(list: ArrayBuffer[String]) => anagramsMap.updated(key, list.addOne(item))
          case None => anagramsMap
        }
      }
    })

    anagramsMap.foreach((key, values) => {
      println(s"$key: ${values.mkString(", ")}")
    })
  }

}
