package org.marius
package lab1.checkpoint2

import lab1.checkpoint2.minimal.Minimal

object Main extends App {
    Minimal.isPrime(5)

    Minimal.cylinderArea(3,4)

    Minimal.reverse(List(1, 2, 4, 8, 4))

    Minimal.uniqueSum(List(1, 2, 4, 8, 4, 2))

    Minimal.extractRandomN(List(1, 2, 4, 8, 4), 3)

    Minimal.firstFibonacciElements(7)

    Minimal.translator(Map("mama" -> "mother", "papa" -> "father"), "mama is with papa")

    Minimal.smallestNumber(4, 5, 3)
    Minimal.smallestNumber(0, 3, 4)

}
