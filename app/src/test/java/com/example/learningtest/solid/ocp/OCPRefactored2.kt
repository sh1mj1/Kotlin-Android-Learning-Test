package com.example.learningtest.solid.ocp

class OCPRefactored2 {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> = lottoSeller.soldLotto(money)
    }

    abstract class LottoSeller {
        abstract val lottoPrice: Int

        fun soldLotto(money: Int): List<Lottery> {
            val count = money / lottoPrice
            return List(count) { LotteryGenerateStrategy().lotto() }
        }
    }

    class NormalLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    data class Lottery(val numbers: List<Int>) {
        init {
            numbers.forEach {
                require(numbers.size == NUMBER_COUNT) {
                    "Invalid lotto number count"
                }
                require(it in MIN_NUMBER..MAX_NUMBER) {
                    "Invalid lotto number"
                }
            }
        }

        companion object {
            private const val MIN_NUMBER = 1
            private const val MAX_NUMBER = 45
            val numberRange = (MIN_NUMBER..MAX_NUMBER)

            const val NUMBER_COUNT = 6
        }
    }

    class LotteryGenerateStrategy {
        fun lotto(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            )
    }
}
