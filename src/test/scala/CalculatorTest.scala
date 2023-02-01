package org.marius

import lab1.Calculator

import org.scalatest.funsuite.AnyFunSuite

class CalculatorTest extends AnyFunSuite{
  test("CubeCalculator.square") {
    assert(Calculator.square(2) === 4)
  }

  test("CubeCalculator.cube") {
    assert(Calculator.cube(2) === 8)
  }
}
