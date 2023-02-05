package org.marius
package lab1.checkpoint2.bonus

object Bonus {
  def commonPrefix(list: List[String]): Unit = {
    var commonPrefix = list(0)

    for (i <- 1 to list.length - 1 by 1) {
      while (list(i).indexOf(commonPrefix) != 0) {
        commonPrefix = commonPrefix.substring(0, commonPrefix.length - 1)

        if (commonPrefix.isEmpty) {
          println("")
          return
        }
      }
    }

    println(commonPrefix)
  }

}
