package com.example.learningtest.solid.dip

import com.example.learningtest.solid.dip.DIPRefactored2.ChatCapable
import com.example.learningtest.solid.dip.DIPRefactored2.ChatbotLottoSeller
import com.example.learningtest.solid.dip.DIPRefactored2.Customer
import com.example.learningtest.solid.dip.DIPRefactored2.DisCountLottoSeller
import com.example.learningtest.solid.dip.DIPRefactored2.Lottery
import com.example.learningtest.solid.dip.DIPRefactored2.ManualLottoGenerateStrategy
import com.example.learningtest.solid.dip.DIPRefactored2.NoisyLottoVendingMachine
import com.example.learningtest.solid.dip.DIPRefactored2.NormalLottoSeller
import com.example.learningtest.solid.dip.DIPRefactored2.NormalLottoVendingMachine
import com.example.learningtest.solid.dip.DIPRefactored2.Rectangle
import com.example.learningtest.solid.dip.DIPRefactored2.ResetCapable
import com.example.learningtest.solid.dip.DIPRefactored2.Square
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DIPRefactored2Test : BehaviorSpec({

    Given("ChatCapable") {
        val chatCapable = ChatCapable("Chatting -- Hello!")

        Then("onRest() 호출 시 ChatCapable은 채팅 메시지를 반환한다") {
            chatCapable.onRest() shouldBe "Chatting -- Hello!"
        }
    }

    Given("ResetCapable") {
        val resetCapable = ResetCapable("Resetting -- Quietly")

        Then("onRest() 호출 시 ResetCapable은 초기화 메시지를 반환한다") {
            resetCapable.onRest() shouldBe "Resetting -- Quietly"
        }
    }

    Context("일반 로또 판매자에게") {
        val customer = Customer()
        val seller = NormalLottoSeller()
        Given("5 장의 정사각형 모양의 로또를 구매하려고 한다") {
            val shape = Square(3)
            val strategies =
                List(5) { ManualLottoGenerateStrategy(listOf(1, 2, 3, 4, 5, 6), shape) }

            When("5,000 원을 지불하면") {
                val money = 5_000
                val lotteries = customer.buyLotto(money, seller, strategies)
                Then("5 장의 정사각형 로또를 받는다") {
                    lotteries shouldBe
                        List(5) {
                            Lottery(listOf(1, 2, 3, 4, 5, 6), shape)
                        }
                }
            }

            When("4,000 원을 지불하면") {
                val money = 4_000

                Then("구매하려는 로또 개수 4장보다 금액이 부족해 예외가 발생한다") {
                    shouldThrow<IllegalArgumentException> {
                        customer.buyLotto(money, seller, strategies)
                    }.message shouldBe "the number of lottoGenerateStrategies must be same with lotto count"
                }
            }
        }
    }

    Given("로또 판매자(Customer)에게 로또를 구매할 때") {
        val customer = Customer()
        val seller = NormalLottoSeller()
        val shape = Square(3)
        val strategies =
            List(5) { ManualLottoGenerateStrategy(listOf(1, 2, 3, 4, 5, 6), shape) }

        When("5,000원을 지불하고 정확한 수량의 전략을 제공하면") {
            val lotteries = customer.buyLotto(5000, seller, strategies)

            Then("구매한 로또 개수와 전략 개수는 동일하다") {
                lotteries.size shouldBe strategies.size
            }
        }

        When("로또 구매 방식의 수와 구매 가능한 로또 개수가 다르면") {
            val invalidStrategies =
                List(3) {
                    ManualLottoGenerateStrategy(
                        listOf(1, 2, 3, 4, 5, 6),
                        shape,
                    )
                }

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    customer.buyLotto(5000, seller, invalidStrategies)
                }.message shouldBe "the number of lottoGenerateStrategies must be same with lotto count"
            }
        }
    }

    Given("각 로또 판매자의 onRest() 동작을 확인할 때") {
        val normalSeller = NormalLottoSeller()
        val discountSeller = DisCountLottoSeller()
        val vendingMachine = NormalLottoVendingMachine()
        val noisyVendingMachine = NoisyLottoVendingMachine()
        val chatbotSeller = ChatbotLottoSeller()

        Then("일반 로또 판매자는 채팅 메시지를 반환한다") {
            normalSeller.onRest() shouldBe "Chatting -- Hello!"
        }

        Then("할인 로또 판매자는 아침 인사를 반환한다") {
            discountSeller.onRest() shouldBe "Chatting -- Good morning!"
        }

        Then("일반 로또 자판기는 조용한 초기화 메시지를 반환한다") {
            vendingMachine.onRest() shouldBe "Resetting -- Quietly"
        }

        Then("소음이 있는 로또 자판기는 시끄러운 초기화 메시지를 반환한다") {
            noisyVendingMachine.onRest() shouldBe "Resetting -- With noise"
        }

        Then("챗봇 로또 판매자는 채팅과 초기화 메시지를 모두 반환한다") {
            chatbotSeller.onRest() shouldBe
                """
                Chatting -- Hello, You can call me chatbot!
                Resetting -- Quietly
                """.trimIndent()
        }
    }

    Given("ManualLottoGenerateStrategy 수동 로또 전략") {
        val shape = Rectangle(3, 4)
        val manualStrategy = ManualLottoGenerateStrategy(listOf(1, 2, 3, 4, 5, 6), shape)

        Then("지정된 번호로 로또를 생성한다") {
            val lottery = manualStrategy.lotto()

            lottery.numbers shouldBe listOf(1, 2, 3, 4, 5, 6)
            lottery.shape shouldBe Rectangle(3, 4)
        }
    }
})
