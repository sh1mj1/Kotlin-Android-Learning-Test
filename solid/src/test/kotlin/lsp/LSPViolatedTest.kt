package lsp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import solid.lsp.LSPViolated.Rectangle
import solid.lsp.LSPViolated.Square

class LSPViolatedTest : BehaviorSpec({

    Given("높이 4, 세로 5 의 직사각형(Rectangle)") {
        val rectangle: Rectangle = Rectangle(_height = 4, _width = 5)

        Then("면적은 20 이다") {
            rectangle.area() shouldBe 20
        }

        When("높이 3, 너비 6으로 변경하면") {
            rectangle.changeHeight(6)
            rectangle.changeWidth(3)

            Then("면적은 18 이다") {
                rectangle.area() shouldBe 18
            }
        }
    }

    Given("한변의 길이가 5인 정사각형 정사각형(Square) 클래스가 주어졌을 때") {
        val square: Rectangle = Square(5)

        Then("면적은 25 이다") {
            square.area() shouldBe 25
        }

        When("너비를 4로 설정하면") {
            square.changeWidth(4)

            Then("면적은 16 이다") {
                square.area() shouldBe 16
            }
        }

        When("높이를 6으로 설정하면") {
            square.changeHeight(6)

            Then("면적은 36 이다") {
                square.area() shouldBe 36
            }
        }

        When("가로와 세로를 다른 값으로 설정하면") {
            Then("정사각형의 속성상 항상 동일한 값이 유지되어야 한다").config(enabled = false) {
                square.changeHeight(2)
                square.changeWidth(3)
                square.area() shouldBe 6
            }
        }
    }
})
