package com.example.learningtest.solid

class SRPViolated {
    class LottoSeller {
        fun soldLotto(money: Int): List<Lottery> {
            // calculate lotteries count with money and price
            val count = money / LOTTO_PRICE

            // generate lotteries with random numbers
            val lotteries =
                (1..count)
                    .map { Lottery((lottoRange).shuffled().take(6).sorted()) }

            // validate lotteries
            lotteries.forEach { validate(it) }
            return lotteries
        }

        fun validate(lottery: Lottery) {
            require(lottery.numbers.size == LOTTO_NUMBER_COUNT) { "Invalid lotto number count" }
            lottery.numbers.forEach {
                require(it in MIN_LOTTO_NUMBER..MAX_LOTTO_NUMBER) { "Invalid lotto number" }
            }
        }

        companion object {
            private const val LOTTO_PRICE = 1000
            private const val MIN_LOTTO_NUMBER = 1
            private const val MAX_LOTTO_NUMBER = 45
            private const val LOTTO_NUMBER_COUNT = 6

            private val lottoRange = (MIN_LOTTO_NUMBER..MAX_LOTTO_NUMBER)
        }
    }

    data class Lottery(val numbers: List<Int>)
}
