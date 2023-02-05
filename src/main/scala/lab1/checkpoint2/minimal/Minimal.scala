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

}
