package com.example.learningtest.solid

class OCPViolated {
    private class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
            lottoSellerType: LottoSellerType,
        ): List<Lottery> {
            when (lottoSellerType) {
                LottoSellerType.NORMAL -> {
                    return lottoSeller.soldLotto(money)
                }
                LottoSellerType.ILLEGAL -> {
                    val count = money / ILLEGAL_LOTTO_PRICE
                    return List(count) { LotteryGenerateStrategy().autoGenerate() }
                }
            }
        }

        companion object {
            private const val ILLEGAL_LOTTO_PRICE = 500
        }
    }

    private enum class LottoSellerType {
        NORMAL,
        ILLEGAL,
    }

    private class LottoSeller {
        fun soldLotto(money: Int): List<Lottery> {
            val count = money / LOTTO_PRICE
            return List(count) { LotteryGenerateStrategy().autoGenerate() }
        }

        companion object {
            const val LOTTO_PRICE = 1000
        }
    }

    private data class Lottery(val numbers: List<Int>) {
        init {
            numbers.forEach {
                require(numbers.size == NUMBER_COUNT) { "Invalid lotto number count" }
                require(it in MIN_NUMBER..MAX_NUMBER) { "Invalid lotto number" }
            }
        }

        companion object {
            private const val MIN_NUMBER = 1
            private const val MAX_NUMBER = 45
            val numberRange = (MIN_NUMBER..MAX_NUMBER)

            const val NUMBER_COUNT = 6
        }
    }

    private class LotteryGenerateStrategy {
        fun autoGenerate(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            )
    }
}
