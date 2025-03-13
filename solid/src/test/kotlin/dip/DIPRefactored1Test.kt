package dip

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import solid.dip.DIPRefactored1.ChatCapable
import solid.dip.DIPRefactored1.Customer
import solid.dip.DIPRefactored1.Lottery
import solid.dip.DIPRefactored1.LottoGenerateStrategy
import solid.dip.DIPRefactored1.NormalLottoSeller
import solid.dip.DIPRefactored1.RestActions
import solid.dip.DIPRefactored1.Square

class DIPRefactored1Test : BehaviorSpec({
    Given("고객이 로또를 구매하려고 할 때") {
        val lottoSeller =
            NormalLottoSeller(
                lottoPrice = 1000,
                restActions = RestActions(ChatCapable("Chatting -- Hello!")),
            )
        val customer = Customer()

        When("충분한 금액으로 정상적으로 구매하는 경우") {
            val strategies = List(3) { TestLottoGenerateStrategy() }
            val boughtLotto = customer.buyLotto(3000, lottoSeller, strategies)

            Then("로또가 정상적으로 구매되어야 한다") {
                boughtLotto.size shouldBe 3
                boughtLotto.forEach { lottery ->
                    lottery.numbers shouldBe listOf(1, 2, 3, 4, 5, 6)
                }
            }
        }

        When("금액이 부족한 경우") {
            val strategies = List(1) { TestLottoGenerateStrategy() }

            Then("예외가 발생해야 한다") {
                shouldThrow<IllegalArgumentException> {
                    customer.buyLotto(500, lottoSeller, strategies)
                }
            }
        }

        When("판매자가 휴식 중인 경우") {
            val lottoSeller =
                NormalLottoSeller(
                    lottoPrice = 1000,
                    restActions = RestActions(ChatCapable("Chatting -- Seller resting")),
                )

            val strategies = List(1) { TestLottoGenerateStrategy() }
            val boughtLotto = customer.buyLotto(1000, lottoSeller, strategies)

            Then("로또 구매는 정상적으로 이루어지지만, 휴식 메시지가 출력되어야 한다") {
                boughtLotto.size shouldBe 1
            }
        }
    }
}) {
    class TestLottoGenerateStrategy : LottoGenerateStrategy {
        override fun lotto(): Lottery {
            return Lottery(
                numbers = listOf(1, 2, 3, 4, 5, 6),
                shape = Square(3),
            )
        }
    }
}
