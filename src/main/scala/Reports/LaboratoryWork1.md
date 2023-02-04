# FAF.PTR16.1 -- Project 0
> **Performed by:** Marius Badrajan, group FAF-201<br>
> **Verified by:** asist. univ. Alexandru Osadcenco

## P0W1

**Task 1** -- Write a script that would print the message “Hello PTR” on the screen.

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

Here I have created an object Calculator which contains 2 static methods that computes the square and cube results of a number `x`.

```scala
class CalculatorTest extends AnyFunSuite{
  test("CubeCalculator.square") {
    assert(Calculator.square(2) === 4)
  }

  test("CubeCalculator.cube") {
    assert(Calculator.cube(2) === 8)
  }
}
```

Here I have created a class CalculatorTest which extends the AnyFunSuite class and contains 2 unit tests that tests the functionality of the methods declared in the Calculator object.

## Conclusion

In this laboratory work, I got to know Scala and wrote the first program and the first unit test in this language.

## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html). 
- Installation guide to install [sbt](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html). 
- Getting started with [Scala and sbt](https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html) on the command line.
