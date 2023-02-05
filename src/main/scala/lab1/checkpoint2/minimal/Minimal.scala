package org.marius
package lab1.checkpoint2.minimal

import java.util.Dictionary
import scala.util.Random

object Minimal {
  // check whether a number is prime
  def isPrime(i: Int): Unit = {
    if (i <= 1)
      println("false")
    else if (i == 2)
      println("true")
    else
      println(!(2 to (i-1)).exists(nr => i % nr == 0))
  }

  def cylinderArea(height: Int, radius: Int): Unit = {
    val result = 2 * Math.PI * radius * height + 2 * Math.PI * Math.pow(radius, 2)

    println(f"$result%.4f")
  }

  def reverse(list: List[Int]): Unit = {
    val reversedList: Array[Int] = new Array[Int](list.length)

    for (i <- list.length to 1 by -1) {
      reversedList(list.length - i) = list(i - 1)
    }

    reversedList.foreach(x => print(x + " "))

    // list.reverse.foreach(x => print(x + " "))
    println()
  }

  def uniqueSum(list: List[Int]): Unit = {
    val uniqueValuesSet = list.toSet

    println(uniqueValuesSet.sum)
  }

  def extractRandomN(list: List[Int], n: Int): Unit = {
    val random = Random()
    val randomList: Array[Int] = new Array[Int](n)

    for (i <- 0 to n - 1 by 1) {
      val randomIndex = random.nextInt(list.length)
      randomList(i) = list(randomIndex)
    }

    randomList.foreach(x => print(x + " "))
    println()
  }

  private def fibonacci(n: Int): Int = {
    n match {
      case 0 => 0
      case 1 => 1
      case _ => fibonacci (n - 2) + fibonacci(n - 1)
    }
  }

  def firstFibonacciElements(n: Int): Unit = {
    for (i <- List.range(1, n + 1)) {
      print(fibonacci(i))
      if (i != n) {
        print(", ")
      }
    }

    println()
  }

  def translator(dictionary: Map[String, String], originalString: String): Unit = {
    var newString = originalString;

    dictionary.keys.foreach(key => {
      if (originalString.contains(key)) {
        newString = newString.replace(key, dictionary(key))
      }
    })

    println(newString)
  }

}
