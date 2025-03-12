package ocp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import solid.ocp.OCPViolated2.Customer
import solid.ocp.OCPViolated2.LottoSeller
import solid.ocp.OCPViolated2.LottoSellerType

class OCPViolated2Test : BehaviorSpec({
    context("Customer 소비자가 로또 판매자에게 로또를 구매한다") {
        val customer = Customer()
        Given("LottoSeller") {
            val lottoSeller = LottoSeller()

            When("일반 로또 판매자에게 6000 으로 구매할 때") {
                val money = 6_000
                val lotteries =
                    customer.buyLotto(
                        money,
                        lottoSeller,
                        LottoSellerType.NORMAL,
                    )

                Then("5 장의 로또를 구매한다") {
                    lotteries shouldHaveSize (6)
                }
            }

            When("할인 로또 판매자에게 5000 으로 구매할 때") {
                val money = 5_000
                val lotteries =
                    customer.buyLotto(
                        money,
                        lottoSeller,
                        LottoSellerType.DISCOUNT,
                    )

                Then("10 장의 로또를 구매한다") {
                    lotteries shouldHaveSize (10)
                }
            }
        }
    }
})
