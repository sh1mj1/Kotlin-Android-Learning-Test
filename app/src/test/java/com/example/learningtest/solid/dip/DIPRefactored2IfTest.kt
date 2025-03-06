package com.example.learningtest.solid.dip

import com.example.learningtest.solid.dip.DIPRefactored2If.ChatCapable
import com.example.learningtest.solid.dip.DIPRefactored2If.Customer
import com.example.learningtest.solid.dip.DIPRefactored2If.ManualLottoGenerateStrategy
import com.example.learningtest.solid.dip.DIPRefactored2If.NormalLottoSeller
import com.example.learningtest.solid.dip.DIPRefactored2If.Rectangle
import com.example.learningtest.solid.dip.DIPRefactored2If.ResetCapable
import com.example.learningtest.solid.dip.DIPRefactored2If.RestActions
import com.example.learningtest.solid.dip.DIPRefactored2If.Square
import io.kotest.core.spec.style.BehaviorSpec

/**
 * 만약 로또 판매자의 금액에 대해서도 DIP 를 적용한다면?
 */
class DIPRefactored2IfTest : BehaviorSpec({
    Given("sdf") {
        Customer().buyLotto(
            money = 2_000,
            lottoSeller =
                NormalLottoSeller(
                    restActions =
                        RestActions(
                            ChatCapable(message = "Hello!"),
                            ResetCapable(message = "Resetting..."),
                        ),
                    lottoPrice = 1_000,
                ),
            lottoGenerateStrategies =
                listOf(
                    ManualLottoGenerateStrategy(listOf(1, 2, 3, 4, 5, 6), Square(3)),
                    ManualLottoGenerateStrategy(listOf(11, 12, 23, 34, 45, 16), Rectangle(5, 3)),
                ),
        )
    }
})
