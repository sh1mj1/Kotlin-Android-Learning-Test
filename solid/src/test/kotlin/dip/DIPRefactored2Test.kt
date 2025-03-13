package dip

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import solid.dip.DIPRefactored2.ChatbotLottoSeller
import solid.dip.DIPRefactored2.Customer
import solid.dip.DIPRefactored2.DisCountLottoSeller
import solid.dip.DIPRefactored2.ManualLottoGenerateStrategy
import solid.dip.DIPRefactored2.NormalLottoSeller
import solid.dip.DIPRefactored2.Rectangle
import solid.dip.DIPRefactored2.Square

class DIPRefactored2Test : BehaviorSpec({

    Given("A Customer who wants to buy lotto") {
        val customer = Customer()

        When("buying lotto from a normal lotto seller with valid money and strategies") {
            val seller = NormalLottoSeller()
            val strategies =
                List(3) {
                    ManualLottoGenerateStrategy(
                        listOf(1, 2, 3, 4, 5, 6),
                        Square(4),
                    )
                }

            val result = customer.buyLotto(3_000, seller, strategies)

            Then("the correct number of lotto tickets should be issued") {
                result shouldHaveSize 3
            }

            Then("each lotto ticket should have the correct numbers and shape") {
                result.forEach { lotto ->
                    lotto.numbers shouldBe listOf(1, 2, 3, 4, 5, 6)
                    (lotto.shape as Square).area() shouldBe 16
                }
            }
        }

        When("buying lotto from a discount seller with valid money and strategies") {
            val seller = DisCountLottoSeller()
            val strategies =
                List(2) {
                    ManualLottoGenerateStrategy(
                        listOf(10, 20, 30, 40, 41, 42),
                        Rectangle(2, 5),
                    )
                }

            val result = customer.buyLotto(1_000, seller, strategies)

            Then("the correct number of lotto tickets should be issued") {
                result shouldHaveSize 2
            }

            Then("each lotto ticket should have the correct numbers and shape") {
                result.forEach { lotto ->
                    lotto.numbers shouldBe listOf(10, 20, 30, 40, 41, 42)
                    (lotto.shape as Rectangle).area() shouldBe 10
                }
            }
        }

        When("buying lotto from a seller who requires rest") {
            val seller = ChatbotLottoSeller()
            val strategies =
                List(1) {
                    ManualLottoGenerateStrategy(
                        listOf(5, 10, 15, 20, 25, 30),
                        Square(5),
                    )
                }

            Then("the seller should display a rest message") {
                seller.onRest() shouldBe "Chatting -- Hello, You can call me chatbot!\nResetting -- Quietly"
            }

            val result = customer.buyLotto(1_000, seller, strategies)

            Then("a lotto ticket should be issued after resting") {
                result shouldHaveSize 1
            }
        }
    }
})
