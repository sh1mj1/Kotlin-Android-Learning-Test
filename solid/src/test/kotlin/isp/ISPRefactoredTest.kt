package isp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import solid.isp.ISPRefactored.*

class ISPRefactoredTest : BehaviorSpec({
    Given("Buy lotto with money 10,000") {

        When("Human Normal Lotto Seller") {
            val lottoSeller = NormalLottoSeller()
            val restAction = lottoSeller.chat()

            Then("Customer can buy lotto") {
                restAction shouldBe "Hello!"
            }
        }

        When("Human Discounted Lotto Seller") {
            val lottoSeller = DiscountedLottoSeller()
            val restAction = lottoSeller.chat()

            Then("Customer can buy lotto") {
                restAction shouldBe "Good morning!"
            }
        }

        When("NormalLottoVendingMachine") {
            val lottoSeller = NormalLottoVendingMachine()
            val restAction = lottoSeller.reset()

            Then("Customer can buy lotto") {
                restAction shouldBe "Reset quietly"
            }
        }

        When("NoisyLottoVendingMachine") {
            val lottoSeller = NoisyLottoVendingMachine()
            val restAction = lottoSeller.reset()

            Then("Customer can buy lotto") {
                restAction shouldBe "Reset with noise"
            }
        }
    }
})
