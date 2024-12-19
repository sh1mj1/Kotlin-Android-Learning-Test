package com.example.learningtest.solid

class SRPRefactored {
    private class LottoSeller {
        fun soldLotto(money: Int): List<Lottery> {
            // calculate lotteries count with money and price
            val count = money / LOTTO_PRICE
            return List(count) { LotteryGenerateStrategy().autoGenerate() }
        }

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    private data class Lottery(val numbers: List<Int>) {
        // validate lotteries
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

    // generate lottery with random numbers
    private class LotteryGenerateStrategy {
        fun autoGenerate(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            )
    }
}
