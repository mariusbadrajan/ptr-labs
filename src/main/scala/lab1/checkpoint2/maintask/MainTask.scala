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
  
}
