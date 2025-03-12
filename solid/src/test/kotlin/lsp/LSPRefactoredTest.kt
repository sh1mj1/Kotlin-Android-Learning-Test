package lsp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import solid.lsp.LSPRefactored.Rectangle
import solid.lsp.LSPRefactored.Shape
import solid.lsp.LSPRefactored.Square

class LSPRefactoredTest : BehaviorSpec({

    Given("가로 4, 세로 5인 직사각형(Rectangle)") {
        val rectangle = Rectangle(4, 5)

        Then("면적은 20이다") {
            rectangle.area() shouldBe 20
        }

        When("너비를 6으로 변경하면") {
            rectangle.setWidth(6)

            Then("면적은 30이 된다") {
                rectangle.area() shouldBe 30
            }

            Then("Shape 타입으로도 면적이 18이 된다") {
                (rectangle as Shape).area() shouldBe 30
            }
        }
    }

    Given("한 변의 길이가 5인 정사각형(Square)") {
        val square = Square(5)

        Then("면적은 25이다") {
            square.area() shouldBe 25
        }

        When("한 변의 길이를 4로 변경하면") {
            square.setSide(4)

            Then("면적은 16이 된다") {
                square.area() shouldBe 16
            }

            Then("Shape 타입으로도 면적이 16이 된다") {
                (square as Shape).area() shouldBe 16
            }
        }
    }
})
