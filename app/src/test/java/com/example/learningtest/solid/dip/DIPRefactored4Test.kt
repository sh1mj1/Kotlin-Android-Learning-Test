package com.example.learningtest.solid.dip

import com.example.learningtest.solid.dip.DIPRefactored4.Customer
import com.example.learningtest.solid.dip.DIPRefactored4.ManualLottoGenerateStrategy
import com.example.learningtest.solid.dip.DIPRefactored4.NormalLottoSeller
import com.example.learningtest.solid.dip.DIPRefactored4.Rectangle
import com.example.learningtest.solid.dip.DIPRefactored4.Square

class DIPRefactored4Test {
    init {
        val customer = Customer()

        customer.buyLotto(
            money = 2_000,
            lottoSeller = NormalLottoSeller(),
            lottoGenerateStrategies =
                listOf(
                    ManualLottoGenerateStrategy(listOf(1, 2, 3, 4, 5, 6), Square(3)),
                    ManualLottoGenerateStrategy(listOf(11, 12, 23, 34, 45, 16), Rectangle(5, 3)),
                ),
        )
    }
}
