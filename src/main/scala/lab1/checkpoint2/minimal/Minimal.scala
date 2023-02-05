package org.marius
package lab1.checkpoint2.minimal

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
  }

}
