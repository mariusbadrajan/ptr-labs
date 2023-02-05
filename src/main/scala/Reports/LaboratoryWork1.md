# FAF.PTR16.1 -- Project 0

> **Performed by:** Marius Badrajan, group FAF-201<br>
> **Verified by:** asist. univ. Alexandru Osadcenco

## P0W1

**Task 1 (Minimal Task)** -- Write a script that would print the message “Hello PTR” on the screen.

```scala
object Main {
  def main(args: Array[String]) = {
    println("Hello PTR")
  }
}
```

Here I have created an object Main which contains a static method that displays in the console "Hello PTR".
> The `object Main` creates a singleton object `Main` as instance of some anonymous class.<br>
> It is used to hold the static member `main` that is not associated with instances of some class.

**Task 2 (Bonus Task)** -- Create a unit test for your project.

```scala
object Calculator {
  def square(x: Int) = x * x;

  def cube(x: Int) = x * x * x;
}
```

Here I have created an object Calculator which contains 2 static methods that computes the square and cube results of a
number `x`.

```scala
class CalculatorTest extends AnyFunSuite {
  test("CubeCalculator.square") {
    assert(Calculator.square(2) === 4)
  }

  test("CubeCalculator.cube") {
    assert(Calculator.cube(2) === 8)
  }
}
```

Here I have created a class CalculatorTest which extends the AnyFunSuite class and contains 2 unit tests that tests the
functionality of the methods declared in the Calculator object.

## P0W2

**Task 1 (Minimal Task)** -- Write a function that determines whether an input integer is prime.

```scala
def isPrime(i: Int): Unit = {
  if (i <= 1)
    println("false")
  else if (i == 2)
    println("true")
  else
    println(!(2 to (i - 1)).exists(nr => i % nr == 0))
}
```

Here goes the explanation of the code from above..

**Task 2 (Minimal Task)** -- Write a function to calculate the area of a cylinder, given it’s height and radius.

```scala
def cylinderArea(height: Int, radius: Int): Unit = {
  val result = 2 * Math.PI * radius * height + 2 * Math.PI * Math.pow(radius, 2)

  println(f"$result%.4f")
}
```

Here goes the explanation of the code from above..

**Task 3 (Minimal Task)** -- Write a function to reverse a list.

```scala
def reverse(list: List[Int]): Unit = {
  val reversedList: Array[Int] = new Array[Int](list.length)

  for (i <- list.length to 1 by -1) {
    reversedList(list.length - i) = list(i - 1)
  }

  reversedList.foreach(x => print(x + " "))

  // list.reverse.foreach(x => print(x + " "))
  println()
}
```

Here goes the explanation of the code from above..

**Task 4 (Minimal Task)** -- Write a function to calculate the sum of unique elements in a list.

```scala
def uniqueSum(list: List[Int]): Unit = {
  val uniqueValuesSet = list.toSet

  println(uniqueValuesSet.sum)
}
```

Here goes the explanation of the code from above..

**Task 5 (Minimal Task)** -- Write a function that extracts a given number of randomly selected elements from a list.

```scala
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
```

Here goes the explanation of the code from above..

**Task 6 (Minimal Task)** -- Write a function that returns the first n elements of the Fibonacci sequence.

```scala
private def fibonacci(n: Int): Int = {
  n match {
    case 0 => 0
    case 1 => 1
    case _ => fibonacci(n - 2) + fibonacci(n - 1)
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
```

Here goes the explanation of the code from above..

**Task 7 (Minimal Task)** -- Write a function that, given a dictionary, would translate a sentence. Words not found in
the dictionary need not be translated.

```scala
def translator(dictionary: Map[String, String], originalString: String): Unit = {
  var newString = originalString;

  dictionary.keys.foreach(key => {
    if (originalString.contains(key)) {
      newString = newString.replace(key, dictionary(key))
    }
  })

  println(newString)
}
```

Here goes the explanation of the code from above..

**Task 8 (Minimal Task)** --Write a function that receives as input three digits and arranges them in an order that
would create the smallest possible number. Numbers cannot start with a 0.

```scala
def smallestNumber(x: Int, y: Int, z: Int): Unit = {
  var arr: Array[Int] = Array[Int](x, y, z)

  arr = arr.sorted

  if (arr(0) == 0) {
    val temp = arr(0)
    arr(0) = arr(1)
    arr(1) = temp
  }

  arr.foreach(print)
  println()
}
```

Here goes the explanation of the code from above..

**Task 9 (Minimal Task)** -- Write a function that would rotate a list n places to the left.

```scala
def rotateLeft(list: List[Int], places: Int): Unit = {
  val rotatedList = list.drop(places % list.length) ::: list.take(places % list.length)

  rotatedList.foreach(x => print(x + " "))
  println()
}
```

Here goes the explanation of the code from above..

**Task 10 (Minimal Task)** -- Write a function that lists all tuples a, b, c such that `a^2 + b^2 = c^2`
and `a, b ≤ 20`.

```scala
def listRightAngleTriangles(): Unit = {
  for (
    a <- 1 to 20 by 1;
    b <- a to 20 by 1
  ) {
    val c = math.sqrt(a * a + b * b).toInt
    if (a * a + b * b == c * c) {
      println(s"$a $b $c")
    }
  }
}
```

Here goes the explanation of the code from above..

**Task 11 (Main Task)** -- Write a function that eliminates consecutive duplicates in a list.

```scala
def removeConsecutiveDuplicates(list: List[Int]): Unit = {
  val removedConsecutiveDuplicatesArr: ArrayBuffer[Int] = new ArrayBuffer[Int]()
  removedConsecutiveDuplicatesArr.addOne(list(0))

  for (i <- 1 to list.length - 1 by 1) {
    if (list(i) != list(i - 1)) {
      removedConsecutiveDuplicatesArr.addOne(list(i))
    }
  }

  removedConsecutiveDuplicatesArr.foreach(x => print(x + " "))
  println()
}
```

Here goes the explanation of the code from above..

**Task 12 (Main Task)** -- Write a function that, given an array of strings, will return the words that can be typed
using only one row of the letters on an English keyboard layout.

```scala
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
```

Here goes the explanation of the code from above..

**Task 13 (Main Task)** -- Create a pair of functions to encode and decode strings using the Caesar cipher.

```scala
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
```

Here goes the explanation of the code from above..

**Task 14 (Main Task)** -- White a function that, given a string of digits from 2 to 9, would return all possible letter
combinations that the number could represent (think phones with buttons).

```scala
def lettersCombinations(digits: String): Unit = {
  val result: ArrayBuffer[String] = new ArrayBuffer[String]()
  val phoneNumbers = Map(
    '2' -> "abc", '3' -> "def", '4' -> "ghi", '5' -> "jkl",
    '6' -> "mno", '7' -> "pqrs", '8' -> "tuv", '9' -> "wxyz")

  traverse(0, "")

  def traverse(depth: Int, currentString: String): Unit = {
    if (currentString.length == digits.length) {
      result.addOne(currentString)
      return
    }
    val currentKey = digits.charAt(depth)
    for (i <- 0 to phoneNumbers(currentKey).length - 1 by 1) {
      traverse(depth + 1, currentString + phoneNumbers(currentKey).charAt(i))
    }
  }

  result.foreach(x => print(x + " "))
  println()
}
```

Here goes the explanation of the code from above..

**Task 15 (Main Task)** -- White a function that, given an array of strings, would group the anagrams together.

```scala
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
```

Here goes the explanation of the code from above..

**Task 16 (Bonus Task)** -- Write a function to find the longest common prefix string amongst a list of strings.

```scala
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
```

Here goes the explanation of the code from above..

**Task 17 (Bonus Task)** -- Write a function to convert arabic numbers to roman numerals.

```scala
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
```

Here goes the explanation of the code from above..

**Task 18 (Bonus Task)** -- Write a function to calculate the prime factorization of an integer.

```scala
def factorize(number: Int): Unit = {
  var currentNumber = number
  val primes: ListBuffer[Int] = new ListBuffer[Int]()

  for (i <- 2 to currentNumber by 1) {
    while (currentNumber % i == 0) {
      primes.addOne(i)
      currentNumber /= i
    }
  }
  primes.foreach(x => print(x + " "))
  println()
}
```

Here goes the explanation of the code from above..

## Conclusion

In this laboratory work, I got to know Scala and wrote the first program and the first unit test in this language.

## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Installation guide to install [sbt](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html).
- Getting started
  with [Scala and sbt](https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html)
  on the command line.
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [api](https://scala-lang.org/api/3.x/).