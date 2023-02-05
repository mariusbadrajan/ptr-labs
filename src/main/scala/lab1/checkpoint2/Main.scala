package org.marius
package lab1.checkpoint2

import lab1.checkpoint2.bonus.Bonus
import lab1.checkpoint2.maintask.MainTask
import lab1.checkpoint2.minimal.Minimal

object Main extends App {
    println("Minimal:")
    minimal()
    println()

    println("Main:")
    main()
    println()

    println("Bonus:")
    bonus()
    println()

    def minimal() = {
        Minimal.isPrime(5)

        Minimal.cylinderArea(3, 4)

        Minimal.reverse(List(1, 2, 4, 8, 4))

        Minimal.uniqueSum(List(1, 2, 4, 8, 4, 2))

        Minimal.extractRandomN(List(1, 2, 4, 8, 4), 3)

        Minimal.firstFibonacciElements(7)

        Minimal.translator(Map("mama" -> "mother", "papa" -> "father"), "mama is with papa")

        Minimal.smallestNumber(4, 5, 3)
        Minimal.smallestNumber(0, 3, 4)

        Minimal.rotateLeft(List(1, 2, 4, 8, 4), 3)

        Minimal.listRightAngleTriangles()
    }

    def main() = {
        MainTask.removeConsecutiveDuplicates(List(1, 2, 2, 2, 4, 8, 4))

        MainTask.lineWords(List("Hello", "Alaska", "Dad", "Peace"))

        MainTask.encode("lorem", 3)
        MainTask.decode("oruhp", 3)

        MainTask.groupAnagrams(List("eat", "tea", "tan", "ate", "nat", "bat"))
    }

    def bonus() = {
        Bonus.commonPrefix(List("flower", "flow", "flight"))
        Bonus.commonPrefix(List("alpha", "beta", "gamma"))

        Bonus.toRoman("13")

        Bonus.factorize(13)
        Bonus.factorize(42)
    }
}
