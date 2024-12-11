package com.example.learningtest.solid

class SRPRefactored {
    class LottoSeller {
        fun soldLotto(money: Int): List<Lottery> {
            // calculate lotteries count with money and price
            val count = money / 1000
            return (1..count).map { LotteryGenerateStrategy().autoGenerate() }
        }
    }

    data class Lottery(val numbers: List<Int>) {
        // validate lotteries
        init {
            numbers.forEach {
                require(numbers.size == LOTTO_NUMBER_COUNT) { "Invalid lotto number count" }
                require(it in MIN_LOTTO_NUMBER..MAX_LOTTO_NUMBER) { "Invalid lotto number" }
            }
        }

        companion object {
            private const val MIN_LOTTO_NUMBER = 1
            private const val MAX_LOTTO_NUMBER = 45
            val numberRange = (MIN_LOTTO_NUMBER..MAX_LOTTO_NUMBER)

            const val LOTTO_NUMBER_COUNT = 6
        }
    }

    // generate lottery with random numbers
        class LotteryGenerateStrategy {
        fun autoGenerate(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.LOTTO_NUMBER_COUNT).sorted(),
            )
    }
}
