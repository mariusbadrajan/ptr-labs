package org.marius

import lab1.checkpoint1.bonus.Calculator
import lab1.checkpoint1.minimal.Main

import org.scalatest.funsuite.AnyFunSuite

class CalculatorTest extends AnyFunSuite{
  test("CubeCalculator.square") {
    assert(Calculator.square(2) === 4)
  }

  test("CubeCalculator.cube") {
    assert(Calculator.cube(2) === 8)
  }

  test("Main.helloPtr") {
    assert(Main.helloPtr() == "Hello Ptr")
  }
}
