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

  def toRoman(arabicNumber: String): Unit = {
    var romanNumber = ""
    var remainingToCompute = arabicNumber.toInt

    // list of tuples
    // why list?, to be able to iterate in descending order
    // why not with Map?, with Map will not work as supposed (in a Map items are not ordered as declared)
    val romanNumerals = List(
      1000 -> "M", 900 -> "CM", 500 -> "D", 400 -> "CD", 100 -> "C",
      90 -> "XC", 50 -> "L", 40 -> "XL", 10 -> "X",
      9 -> "IX", 5 -> "V", 4 -> "IV", 1 -> "I"
    )

    romanNumerals.foreach(pair => {
      while (remainingToCompute >= pair._1) {
        romanNumber += pair._2
        remainingToCompute -= pair._1
      }
    })

    println(romanNumber)
  }

}
