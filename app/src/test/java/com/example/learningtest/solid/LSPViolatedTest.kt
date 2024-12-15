package com.example.learningtest.solid

import io.kotest.assertions.AssertionFailedError
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class LSPViolatedTest : BehaviorSpec({
    Given("A rectangle, type of it is Rectangle") {
        val rectangle: LSPViolated.Rectangle = LSPViolated.Rectangle()
        When("set height 2 and width 5") {
            rectangle.setHeight(2)
            rectangle.setWidth(5)
            Then("area should be 10") {
                rectangle.area() shouldBe 10
            }
        }
    }

    Given("A square, type of it is Rectangle(super type of Square)") {
        val square: LSPViolated.Rectangle = LSPViolated.Square()

        When("set width 2 and height 5") {
            square.setWidth(2)
            square.setHeight(5)
            Then("area should be 10 but it is 25") {
                shouldThrow<AssertionFailedError> {
                    square.area() shouldBe 10
                }
                square.area() shouldBe 25
            }
        }

        When("set width 5") {
            square.setWidth(5)
            Then("area should be 25") {
                square.area() shouldBe 25
            }
        }
        When("set height 2") {
            square.setHeight(2)
            Then("area should be 4") {
                square.area() shouldBe 4
            }
        }
    }
})
