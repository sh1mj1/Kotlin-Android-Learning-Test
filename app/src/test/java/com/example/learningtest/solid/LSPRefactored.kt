package com.example.learningtest.solid

class LSPRefactored {
    private class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> = lottoSeller.soldLotto(money)
    }

    private interface LottoSeller {
        fun soldLotto(money: Int): List<Lottery>
    }

    private class DiscountedLottoSeller : LottoSeller {
        override fun soldLotto(money: Int): List<Lottery> {
            val count = money / LOTTO_PRICE
            return List(count) { LotteryGenerateStrategy().autoGenerate() }
        }

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    private class NormalLottoSeller : LottoSeller {
        override fun soldLotto(money: Int): List<Lottery> {
            val count = money / LOTTO_PRICE
            return List(count) { LotteryGenerateStrategy().autoGenerate() }
        }

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    private data class Lottery(val numbers: List<Int>, val shape: Shape = Square()) {
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

    private interface Shape {
        fun area(): Int
    }

    private class Rectangle(var height: Int = 0, var width: Int = 0) : Shape {
        override fun area(): Int = height * width
    }

    private class Square(var side: Int = 0) : Shape {
        override fun area(): Int = side * side
    }

    private class LotteryGenerateStrategy {
        fun autoGenerate(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            )
    }
}
