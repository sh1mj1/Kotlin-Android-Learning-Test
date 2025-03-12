package srp

import SRPViolated.Lottery
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SRPViolatedTest : BehaviorSpec({

    Given("LottoSeller 클래스") {
        val lottoSeller = SRPViolated.LottoSeller()

        When("5000 원으로 로또를 구매하면") {
            val money = 5000
            val lotteries = lottoSeller.soldLotto(money)

            Then("5개의 로또가 생성된다") {
                lotteries.size shouldBe (5)
            }
        }

        When("로또 숫자가 유효한 범위를 벗어났을 경우") {
            val invalidLottery = Lottery(listOf(1, 2, 3, 4, 5, 50))

            Then("유효하지 않은 로또 숫자 예외가 발생한다") {
                TODO("validate 메서드가 priave ... ")
            }
        }

        When("로또 숫자에 중복이 있을 경우") {
            val invalidLottery = Lottery(listOf(1, 2, 3, 4, 5, 5))

            Then("유효하지 않은 로또 숫자 중복 예외가 발생한다") {
                TODO("validate 메서드가 priave ... ")
            }
        }
        When("로또 숫자의 개수가 부족할 경우") {
            val invalidLottery = Lottery(listOf(1, 2, 3, 4, 5))

            Then("유효하지 않은 로또 숫자 개수 예외가 발생한다") {
                TODO("validate 메서드가 priave ... ")
            }
        }
    }
})
