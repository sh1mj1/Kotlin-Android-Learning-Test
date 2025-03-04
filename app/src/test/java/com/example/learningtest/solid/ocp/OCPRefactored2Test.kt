package com.example.learningtest.solid.ocp

import com.example.learningtest.solid.ocp.OCPRefactored2.Customer
import com.example.learningtest.solid.ocp.OCPRefactored2.DisCountLottoSeller
import com.example.learningtest.solid.ocp.OCPRefactored2.NormalLottoSeller
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize

class OCPRefactored2Test : BehaviorSpec({

    context("Customer 소비자가 로또 판매자에게 로또를 구매한다") {
        val customer = Customer()

        Given("일반 로또 판매자에게 구매한다") {
            val lottoSeller = NormalLottoSeller()

            When("6000 원으로 구매할 때") {
                val money = 6_000
                val lotteries = customer.buyLotto(money, lottoSeller)

                Then("6 장의 로또를 구매한다") {
                    lotteries shouldHaveSize 6
                }
            }
        }

        Given("할인 로또 판매자에게 구매한다") {
            val lottoSeller = DisCountLottoSeller()

            When("6000 원으로 구매할 때") {
                val money = 6_000
                val lotteries = customer.buyLotto(money, lottoSeller)

                Then("12 장의 로또를 구매한다") {
                    lotteries shouldHaveSize 12
                }
            }
        }
    }
})
