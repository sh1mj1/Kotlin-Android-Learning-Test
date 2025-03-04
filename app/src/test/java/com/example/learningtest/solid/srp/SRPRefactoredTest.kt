package com.example.learningtest.solid.srp

import com.example.learningtest.solid.srp.SRPRefactored.Lottery
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class SRPRefactoredTest : BehaviorSpec({

    Given("Lottery 클래스") {
        When("유효한 로또 숫자들이 주어짐") {
            val validNumbers = listOf(1, 2, 3, 4, 5, 6)
            val lottery = Lottery(validNumbers)

            Then("Lottery 인스턴스를 성공적으로 생성") {
                lottery.numbers shouldBe validNumbers
            }
        }

        When("유효한 숫자 범위를 벗어난 로또 숫자를 가짐") {
            val outOfRangeNumbers = listOf(1, 2, 3, 4, 5, 50)

            Then("로또 숫자가 유효하지 않다는 예외를 던진다") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        Lottery(outOfRangeNumbers)
                    }
                exception.message shouldBe "Invalid lotto number"
            }
        }

        When("로또의 숫자의 개수가 적을 때") {
            val invalidNumbers = listOf(1, 2, 3, 4, 5)

            Then("로또 숫자 개수가 유효하지 않다는 예외를 던진다") {
                val exception =
                    shouldThrow<IllegalArgumentException> {
                        Lottery(invalidNumbers)
                    }
                exception.message shouldBe "Invalid lotto number count"
            }
        }
    }

    Given("LottoSeller 클래스") {
        val lottoSeller = SRPRefactored.LottoSeller()

        When("5000 원으로 로또를 구매하면") {
            val money = 5000
            val lotteries = lottoSeller.soldLotto(money)

            Then("5개의 로또가 생성된다") {
                lotteries shouldHaveSize (money / 1000)
            }
        }

        When("로또를 구매할 수 없는 금액이 입력되었을 때") {
            val money = 500

            Then("빈 리스트가 반환된다") {
                val lotteries = lottoSeller.soldLotto(money)
                lotteries shouldBe emptyList()
            }
        }
    }
})
