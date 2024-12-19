package com.example.learningtest.solid

import com.example.learningtest.util.captureOutput
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ISPRefactoredTest : BehaviorSpec({
    Given("Buy lotto with money 10,000") {
        val customer = ISPRefactored.Customer()
        val money = 10_000

        When("Human Normal Lotto Seller") {
            val lottoSeller = ISPRefactored.NormalLottoSeller()

            Then("Customer can buy lotto") {
                val printed =
                    captureOutput { val lotto = customer.buyLotto(money, lottoSeller) }
                printed shouldBe "Hello!"
            }
        }

        When("Human Discounted Lotto Seller") {
            val lottoSeller = ISPRefactored.DiscountedLottoSeller()

            Then("Customer can buy lotto") {
                val printed = captureOutput { customer.buyLotto(money, lottoSeller) }
                printed shouldBe "Good morning!"
            }
        }

        When("NormalLottoVendingMachine") {
            val lottoSeller = ISPRefactored.NormalLottoVendingMachine()

            Then("Customer can buy lotto") {
                val printed = captureOutput { customer.buyLotto(money, lottoSeller) }
                printed shouldBe "Reset quietly"
            }
        }

        When("NoisyLottoVendingMachine") {
            val lottoSeller = ISPRefactored.NoisyLottoVendingMachine()

            Then("Customer can buy lotto") {
                val printed = captureOutput { customer.buyLotto(money, lottoSeller) }
                printed shouldBe "Reset with noise"
            }
        }
    }
})
