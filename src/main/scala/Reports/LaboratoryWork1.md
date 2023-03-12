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

Here I have created a method named `isPrime` that takes an integer i as input and determines whether it is a prime
number.

**Task 2 (Minimal Task)** -- Write a function to calculate the area of a cylinder, given its height and radius.

```scala
def cylinderArea(height: Int, radius: Int): Unit = {
  val result = 2 * Math.PI * radius * height + 2 * Math.PI * Math.pow(radius, 2)

  println(f"$result%.4f")
}
```

Here I have created a method named `cylinderArea` that calculates and prints the total surface area of a cylinder, given
its height and radius.

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

Here I have created a method named `reverse` that takes a list of integers, creates a new array with the same length,
and reverses the order of the elements using a for loop, then prints the reversed list to the console.

**Task 4 (Minimal Task)** -- Write a function to calculate the sum of unique elements in a list.

```scala
def uniqueSum(list: List[Int]): Unit = {
  val uniqueValuesSet = list.toSet

  println(uniqueValuesSet.sum)
}
```

Here I have created a method named `uniqueSum` that takes a list of integers, converts it to a set of unique values,
calculates the sum of those values, and prints it to the console.

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

Here I have created a method named `extractRandomN` that takes a list of integers and an integer `n`, creates a new
array with length `n`, and fills it with `n` random elements from the input list using a `for` loop and the `Random`
class, then prints the array to the console.

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

Here I have created a recursive method named `fibonacci` to compute the nth element in the Fibonacci sequence, and a
public function `firstFibonacciElements` that takes an integer `n`, uses the `fibonacci` function to print the first `n`
elements of the Fibonacci sequence to the console, separated by commas.

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

Here I have created a method named `translator` that takes a map of string translations and an original
string, replaces any keys in the original string with their corresponding values in the map, and prints the resulting
translated string to the console.

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

Here I have created a method named `smallestNumber` that takes three integers, creates an array with those integers,
sorts the array in ascending order, swaps the first and second elements if the first element is 0, then prints the
elements of the array to the console.

**Task 9 (Minimal Task)** -- Write a function that would rotate a list n places to the left.

```scala
def rotateLeft(list: List[Int], places: Int): Unit = {
  val rotatedList = list.drop(places % list.length) ::: list.take(places % list.length)

  rotatedList.foreach(x => print(x + " "))
  println()
}
```

Here I have created a method named `rotateLeft` that takes a list of integers and an integer `places`, rotates the
elements of the list to the left by `places` positions using the `drop` and `take` methods, concatenates the resulting
lists with the `:::` operator, and prints the rotated list to the console.

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

Here I have created a method named `listRightAngleTriangles` that uses two nested `for` loops to iterate over all pairs
of integers between 1 and 20, computes the corresponding value of `c` for each pair using the Pythagorean theorem, and
prints the triplet `(a, b, c)` to the console if it forms a right-angled triangle.

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

Here I have created a method named `removeConsecutiveDuplicates` that takes a list of integers, creates a
new `ArrayBuffer` to store the integers without consecutive duplicates, adds the first element of the input list to
the `ArrayBuffer`, iterates over the input list and adds each element to the `ArrayBuffer` only if it is different from
the previous element, and finally prints the elements of the `ArrayBuffer` to the console.

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

Here I have created a method named `lineWords` that filters words in a list that can be typed using only one row of a
standard QWERTY keyboard.

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

Here I have created a method named `encode` and `decode` that encodes and decodes a word using a Caesar cipher with a
given shift.

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

Here I have created a method named `lettersCombinations` that generates all possible combinations of letters that can be
formed from the given phone number digits.

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

Here I have created a method named `groupAnagrams` that groups a list of strings into anagrams by sorting the characters
of each string and using the sorted version as a key in a map.

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

Here I have created a method named `commonPrefix` that finds the longest common prefix of a list of strings.

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

Here I have created a method named `toRoman` that converts an Arabic number to a Roman numeral.

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

Here I have created a method named `factorize` that factors an integer into a product of prime numbers and prints the
result.

## P0W3

**Task 6 (Main Task)** -- Create a module that would implement a semaphore.

```scala
object SemaphoreModule {
  sealed trait Command

  case class Acquire() extends Command

  case class Release() extends Command

  private val maxValue = 10

  private def apply(initialCount: Int, maxCount: Int = maxValue): Behavior[Command] = Behaviors.setup { context =>
    Behaviors.receiveMessage[Command] {
      case Acquire() =>
        if (initialCount > 0) {
          val newValue = initialCount - 1
          context.log.info(s"Acquired. Remaining $newValue.")
          SemaphoreModule(newValue, maxCount)
        } else {
          Behaviors.same
        }
      case Release() =>
        if (initialCount < maxCount) {
          val newValue = initialCount + 1
          context.log.info(s"Released. Remaining $newValue.")
          SemaphoreModule(newValue, maxCount)
        } else {
          Behaviors.same
        }
    }
  }

  def createSemaphore(initialCount: Int, maxCount: Int = maxValue): ActorRef[Command] = {
    ActorSystem(SemaphoreModule(initialCount, maxCount), "semaphore-module")
  }

  def acquire(semaphoreRef: ActorRef[Command]): Unit = {
    semaphoreRef ! Acquire()
  }

  def release(semaphoreRef: ActorRef[Command]): Unit = {
    semaphoreRef ! Release()
  }
}
```

Here I have created a `SemaphoreModule` that can create semaphores and perform acquire and release operations on them.
The semaphore is implemented as an Akka actor, where the messages it can receive are represented by the `Command` sealed
trait. The semaphore can be created with an initial count and a maximum count. When a thread calls `acquire` on the
semaphore, it will decrement the count and block if the count is already zero. When a thread calls `release`, it will
increment the count and unblock one of the waiting threads if any.

## P0W4

**Task 2 (Main Task)** -- Create a supervised processing line to clean messy strings. The first worker in the line would
split the string by any white spaces (similar to Python’s str.split method). The second actor will lowercase all words
and swap all m’s and n’s (you nomster!). The third actor will join back the sentence with one space between words (
similar to Python’s str.join method). Each worker will receive as input the previous actor’s output, the last actor
printing the result on screen. If any of the workers die because it encounters an error, the whole processing line needs
to be restarted. Logging is welcome.

```scala
object Supervisor {
  sealed trait Command

  case class CleanString(message: String) extends Command

  case object SignalFromChild extends Command

  sealed trait Message

  case class Split(message: String, replyTo: ActorRef[Command]) extends Message

  case class LowerCaseAndSwap(message: List[String], replyTo: ActorRef[Command]) extends Message

  case class Join(message: List[String], replyTo: ActorRef[Command]) extends Message

  def apply(): Behavior[Command] =
    Behaviors
      .setup[Command] { context =>
        var currentMessage = ""

        val thirdWorker = context.spawn(ThirdWorker(), s"ThirdWorker")
        val secondWorker = context.spawn(SecondWorker(thirdWorker), s"SecondWorker")
        val firstWorker = context.spawn(FirstWorker(secondWorker), s"FirstWorker")

        context.watch(firstWorker)
        context.watch(secondWorker)
        context.watch(thirdWorker)

        Behaviors
          .supervise[Command] {
            Behaviors
              .receiveMessagePartial[Command] {
                case CleanString(message) =>
                  currentMessage = message
                  firstWorker ! Split(message, context.self)
                  Behaviors.same
                case SignalFromChild =>
                  context.log.info("A problem occurred while processing the string. Restarting the process...")
                  Thread.sleep(1000)
                  firstWorker ! Split(currentMessage, context.self)
                  Behaviors.same
              }
          }.onFailure[Exception](SupervisorStrategy.restart)
      }
}
```

Here I have created an Akka actor system that supervises a set of child actors,
specifically, `FirstWorker`, `SecondWorker`, and `ThirdWorker`. The Supervisor actor receives `CleanString` messages
containing a string that needs to be cleaned and processed. It forwards this message to its child actors by sending them
a Split message. If any of the child actors fail, the supervisor actor will restart the entire process. Additionally,
the supervisor actor watches over its child actors, which allows it to be notified when any of them have stopped
processing.

```scala
object FirstWorker {
  def apply(nextWorker: ActorRef[Message]): Behavior[Message] =
    Behaviors
      .setup[Message] { context =>
        Behaviors
          .supervise[Message] {
            Behaviors
              .receiveMessagePartial[Message] {
                case Split(message, replyTo) =>
                  try {
                    var originalMessage = message.trim()
                    val splitMessage: ArrayBuffer[String] = new ArrayBuffer[String]()
                    var i = 0
                    while (i < originalMessage.length - 1) {
                      if (originalMessage(i) == ' ') {
                        val currentString = originalMessage.substring(0, i)
                        splitMessage.addOne(currentString)
                        originalMessage = originalMessage.substring(i, originalMessage.length).trim()
                        i = 0
                      }
                      i += 1
                    }
                    splitMessage += originalMessage

                    if (Random.nextBoolean()) {
                      throw new Exception("Random exception from first worker.")
                    }
                    context.log.info(s"First step: `$message` -> `$splitMessage`")
                    nextWorker ! LowerCaseAndSwap(splitMessage.toList, replyTo)
                  } catch {
                    case _: Exception => replyTo ! SignalFromChild
                  }

                  Behaviors.same
              }
          }.onFailure(SupervisorStrategy.restart)
      }
}
```

Here I have created the first Akka actor called FirstWorker. The actor processes messages of type Message and sends them
to the next worker (ActorRef[Message]) for further processing.

The main behavior of the actor is defined in the apply method, which creates a Behaviors.setup that sets up a
supervision hierarchy. The supervision strategy used is restart, which means that if an exception occurs while
processing a message, the actor is restarted and the message is processed again.

The main message that the actor processes is Split, which splits a given message into an array of strings using spaces
as a delimiter. The split message is then sent to the next worker for further processing, with the exception of randomly
throwing an exception.

If an exception is thrown while processing a message, the actor catches it and sends a SignalFromChild message to the
parent actor.

```scala
object SecondWorker {
  def apply(nextWorker: ActorRef[Message]): Behavior[Message] =
    Behaviors
      .setup[Message] { context =>
        Behaviors
          .supervise[Message] {
            Behaviors
              .receiveMessagePartial[Message] {
                case LowerCaseAndSwap(message, replyTo) =>
                  try {
                    val computedWords: ArrayBuffer[String] = new ArrayBuffer[String]()
                    message.foreach(word => {
                      computedWords.addOne(word.toLowerCase().replace("m", "_").replace("n", "m").replace("_", "n"))
                    })

                    if (Random.nextBoolean()) {
                      throw new Exception("Random exception from first worker.")
                    }
                    context.log.info(s"Second step: `$message` -> `$computedWords`")
                    nextWorker ! Join(computedWords.toList, replyTo)
                  } catch {
                    case _: Exception => replyTo ! SignalFromChild
                  }

                  Behaviors.same
              }
          }.onFailure(SupervisorStrategy.restart)
      }
}
```

Here I have created the second Akka actor called SecondWorker that also processes messages of type Message and sends
them to the next worker for further processing.

The main behavior of the actor is defined in the apply method, which creates a Behaviors.setup that sets up a
supervision hierarchy. The supervision strategy used is restart, which means that if an exception occurs while
processing a message, the actor is restarted and the message is processed again.

The main message that the actor processes is LowerCaseAndSwap, which converts each word in the given message to
lowercase and swaps 'm' with 'n' and vice versa. The transformed message is then sent to the next worker for further
processing, with the exception of randomly throwing an exception.

If an exception is thrown while processing a message, the actor catches it and sends a SignalFromChild message to the
parent actor.

```scala
object ThirdWorker {
  def apply(): Behavior[Message] =
    Behaviors
      .setup[Message] { context =>
        Behaviors
          .supervise[Message] {
            Behaviors
              .receiveMessagePartial[Message] {
                case Join(message, replyTo) =>
                  try {
                    var newMessage = ""
                    message.foreach(word => {
                      newMessage += word
                      if (word != message.lastOption.getOrElse("")) {
                        newMessage += " "
                      }
                    })

                    if (Random.nextBoolean()) {
                      throw new Exception("Random exception from third worker.")
                    }
                    context.log.info(s"Third step: `$message` -> `$newMessage`")
                    context.log.info(s"--> Final result: `$newMessage`.`")
                  } catch {
                    case _: Exception => replyTo ! SignalFromChild
                  }

                  Behaviors.same
              }
          }.onFailure(SupervisorStrategy.restart)
      }
}
```

Here I have created the third Akka actor called ThirdWorker that processes messages of type Message.

The main behavior of the actor is defined in the apply method, which creates a Behaviors.setup that sets up a
supervision hierarchy. The supervision strategy used is restart, which means that if an exception occurs while
processing a message, the actor is restarted and the message is processed again.

The main message that the actor processes is Join, which joins the array of strings in the given message with spaces in
between. The joined message is then logged as the final result, with the exception of randomly throwing an exception.

If an exception is thrown while processing a message, the actor catches it and sends a SignalFromChild message to the
parent actor.

## P0W5

**Task 1,2,3 (Minimal Tasks)** -- Create a module that would handle an HTTP request and parse the response.

```scala
object ResponseParser {
  private case class Quote(text: String, author: String, tags: List[String])

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem()

  private val url = "https://quotes.toscrape.com/"
  private val statusCode = "Response status code"
  private val headers = "Response headers"
  private val body = "Response body"
  private val author = "Author"
  private val text = "Text"
  private val tags = "Tags"

  private val responseMap: Map[String, String] = Map[String, String]()
  private var extractedQuotes: List[Map[String, String]] = List[Map[String, String]]()
  private val quotes: ArrayBuffer[Quote] = ArrayBuffer[Quote]()
  private var jsonQuotes: Json = Json.Null

  def parseResponse(): Unit = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url))

    val parsedResponse: Future[Any] = responseFuture.map { response =>
      responseMap += (statusCode -> s"${response.status.intValue}")
      responseMap += (headers -> "")
      response.headers.foreach(header =>
        responseMap(headers) += s"${header.name}: ${header.value}"
      if (header != response.headers.lastOption.getOrElse("")) {
        responseMap(headers) += "\n"
      }
      )
      val responseBody: Future[Map[String, String]] = response.entity.toStrict(1.seconds)
        .flatMap { strictEntity =>
          val byteString = strictEntity.data
          val utf8String = byteString.utf8String
          val convertedString = convertHtmlApostropheEntity(utf8String)
          responseMap += (body -> s"$convertedString")
          Future.successful(responseMap)
        }

      Await.ready(responseBody, 10.seconds)
    }
    Await.ready(parsedResponse, 10.seconds)
  }

  def extractQuotes(): Unit = {
    val quoteBlockRegex = "<div class=\"quote\".*?>([\\s\\S]*?)<\\/div>\\s*?<\\/div>".r
    val quoteTextRegex = "<span class=\"text\".*?>(.*?)<\\/span>".r
    val quoteAuthorRegex = "<span.*?class=\"author\".*?>(.*?)<\\/small>[\\s\\S]*?<\\/span>".r
    val quoteTagRegex = "<a class=\"tag\".*?>(.*?)<\\/a>".r

    val responseBody = responseMap(body)
    val parsedQuotes = quoteBlockRegex.findAllIn(responseBody).toList

    extractedQuotes = parsedQuotes.map { quote =>
      val author = quoteAuthorRegex.findFirstMatchIn(quote).map(_.group(1)).getOrElse("")
      val text = quoteTextRegex.findFirstMatchIn(quote).map(_.group(1)).getOrElse("")
      val tags = quoteTagRegex.findAllMatchIn(quote).map(_.group(1)).mkString(", ")

      val tagsArray = tags.split(", ")
      quotes.addOne(Quote(text.substring(1, text.length - 1), author, tagsArray.toList))

      Map(this.author -> author, this.text -> text, this.tags -> tags)
    }
  }

  def convertToJson(): Unit = {
    jsonQuotes = listToJson(quotes.toList)
  }

  def printParsedResponse(): Unit = {
    println("--> HTTP Response <--")
    println(s"Response status code: ${responseMap(statusCode)}")
    val responseHeaders = responseMap(headers).split("\n")
    println("Response headers:")
    responseHeaders.foreach(header => println(s"\t$header"))
    println(s"Response body:\n${responseMap(body)}")
    println()
  }

  def printExtractedQuotes(): Unit = {
    println("--> Parsed quotes <--")
    extractedQuotes.zipWithIndex.foreach { (item, index) =>
      val parsedItem = parseAnyToMap(item).getOrElse(Map.empty)
      println(s"${index + 1}:")
      println(s"\tQuote: ${parsedItem(text).substring(1, parsedItem(text).length - 1)};")
      println(s"\tAuthor: ${parsedItem(author)};")
      println(s"\tTags: ${parsedItem(tags)};")
    }
    println()
  }

  def printJsonQuotes(): Unit = {
    println("--> Converted quotes into JSON <--")
    println(jsonQuotes)
  }

  private def listToJson(quotes: List[Quote]): Json = {
    given Encoder
    [Quote
    ] = Encoder.forProduct3("text", "author", "tags")(q => (q.text, q.author, q.tags))

    given Encoder
    [List[Quote]
    ] = list =>
      Json.fromValues(list.map(_.asJson))

      quotes.asJson
  }

  private val convertHtmlApostropheEntity: String => String =
    (inputString: String) => inputString.replaceAll("&#39;", "'")

  private val parseAnyToMap: Any => Option[Map[String, String]] =
    (anyValue: Any) => anyValue match {
      case map: Map[String, String] => Some(map)
      case _ => None
    }
}
```

Here I have created an object named `ReponseParser` which includes several methods to scrape quotes from a website,
parse the response, extract the quotes from the response, and convert the extracted quotes into JSON format.

The object ResponseParser contains a case class named Quote which is used to store the extracted quotes. It also defines
several private variables and methods such as url, statusCode, headers, body, author, text, tags, responseMap,
extractedQuotes, quotes, and jsonQuotes.

The methods included in the object are parseResponse() which sends an HTTP request and parses the response,
extractQuotes() which extracts quotes from the response and stores them in the quotes list, convertToJson() which
converts the extracted quotes into JSON format, printParsedResponse() which prints the parsed HTTP response,
printExtractedQuotes() which prints the extracted quotes, and printJsonQuotes() which prints the converted JSON quotes.

The object ResponseParser also includes two private helper methods: listToJson() which converts a list of Quote objects
to JSON format and parseAnyToMap() which is used to convert any value to a map of strings.

## Conclusion

In this laboratory work, I have explored various concepts and techniques related to programming in Scala. I started by
reviewing the basic syntax and data types in Scala, and then moved on to more advanced topics such as functional
programming, pattern matching, and concurrency.

I also worked with some common libraries in Scala, including Akka and Http, and learned how to use them to build
scalable and responsive applications.

Throughout the laboratory work, I have practiced my skills by working on several coding exercises, which challenged me
to apply the concepts and techniques to solve real-world problems.

Overall, this laboratory work provided me with a solid foundation in Scala programming and equipped myself with the
skills and knowledge to build robust, scalable, and maintainable applications using this powerful language.

## Bibliography

### Basic

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Installation guide to install [sbt](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html).
- Getting started
  with [Scala and sbt](https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html)
  on the command line.
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [api](https://scala-lang.org/api/3.x/).

### Akka

- Akka [docs](https://doc.akka.io/docs/akka/current/index.html).
- Akka [api](https://doc.akka.io/api/akka/current/index.html).
- Akka [japi](https://doc.akka.io/japi/akka/current/index.html).
- Akka [http](https://doc.akka.io/docs/akka-http/current/index.html).

### Http
- Http [docs](https://doc.akka.io/docs/akka-http/current/index.html)

### Regex

- Regex online - [first source](https://regexr.com/).
- Regex online - [second source](https://regex101.com/).
- Regex IntelliJ IDEA [docs](https://www.jetbrains.com/help/idea/regular-expressions.html).