package isp

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import solid.isp.ISPViolated.DisCountLottoSeller
import solid.isp.ISPViolated.NoisyLottoVendingMachine
import solid.isp.ISPViolated.NormalLottoSeller
import solid.isp.ISPViolated.NormalLottoVendingMachine

class ISPViolatedTest : BehaviorSpec({
    Given("일반 로또 판매자 NormalLottoSeller") {
        val normalSeller = NormalLottoSeller()

        Then("로또 가격은 1000원이다") {
            normalSeller.lottoPrice shouldBe 1000
        }

        Then("대화가 가능하다") {
            normalSeller.chat() shouldBe "Hello!"
        }

        Then("리셋을 시도하면 예외가 발생한다") {
            shouldThrow<IllegalStateException> { normalSeller.reset() }
        }

        When("로또를 3000원어치 구매하면") {
            val tickets = normalSeller.soldLotto(3000)

            Then("3장의 로또가 생성된다") {
                tickets.size shouldBe 3
            }
        }
    }

    Given("할인 로또 판매자가 주어졌을 때") {
        val discountSeller = DisCountLottoSeller()

        Then("로또 가격은 500원이다") {
            discountSeller.lottoPrice shouldBe 500
        }

        When("로또를 2000원어치 구매하면") {
            val tickets = discountSeller.soldLotto(2000)

            Then("4장의 로또가 생성된다") {
                tickets.size shouldBe 4
            }
        }

        Then("대화가 가능하다") {
            discountSeller.chat() shouldBe "Good morning!"
        }

        Then("리셋을 시도하면 예외가 발생한다") {
            shouldThrow<IllegalStateException> { discountSeller.reset() }
        }
    }

    Given("일반 로또 자판기가 주어졌을 때") {
        val vendingMachine = NormalLottoVendingMachine()

        Then("로또 가격은 1000원이다") {
            vendingMachine.lottoPrice shouldBe 1000
        }

        When("로또를 5000원어치 구매하면") {
            val tickets = vendingMachine.soldLotto(5000)

            Then("5장의 로또가 생성된다") {
                tickets.size shouldBe 5
            }
        }

        Then("대화 시도를 하면 예외가 발생한다") {
            shouldThrow<IllegalStateException> { vendingMachine.chat() }
        }

        Then("리셋하면 'Reset quietly' 메시지를 반환한다") {
            vendingMachine.reset() shouldBe "Reset quietly"
        }
    }

    Given("시끄러운 로또 자판기가 주어졌을 때") {
        val noisyMachine = NoisyLottoVendingMachine()

        Then("로또 가격은 1000원이다") {
            noisyMachine.lottoPrice shouldBe 1000
        }

        When("로또를 2000원어치 구매하면") {
            val tickets = noisyMachine.soldLotto(2000)

            Then("2장의 로또가 생성된다") {
                tickets.size shouldBe 2
            }
        }

        Then("대화 시도를 하면 예외가 발생한다") {
            shouldThrow<IllegalStateException> { noisyMachine.chat() }
        }

        Then("리셋하면 'Reset with noise' 메시지를 반환한다") {
            noisyMachine.reset() shouldBe "Reset with noise"
        }
    }
})
