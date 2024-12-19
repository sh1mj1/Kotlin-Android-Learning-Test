package com.example.learningtest.solid

class DIPRefactored {
    private class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> = lottoSeller.soldLotto(money)
    }

    private abstract class LottoSeller {
        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        abstract val lottoPrice: Int

        abstract fun soldLotto(money: Int): List<Lottery>
    }

    private abstract class HumanLottoSeller : LottoSeller() {
        override fun soldLotto(money: Int): List<Lottery> {
            if (restRequired) println(chat())
            return List(money / lottoPrice) { LotteryGenerateStrategy().autoGenerate() }
        }

        abstract fun chat(): String
    }

    private abstract class MachineLottoSeller : LottoSeller() {
        override fun soldLotto(money: Int): List<Lottery> {
            if (restRequired) reset()
            return List(money / lottoPrice) { LotteryGenerateStrategy().autoGenerate() }
        }

        abstract fun reset(): String
    }

    private class NormalLottoSeller : HumanLottoSeller() {
        override val lottoPrice: Int = 1_000

        override fun chat(): String = "Hello!"
    }

    private class DiscountedLottoSeller : HumanLottoSeller() {
        override val lottoPrice: Int = 500

        override fun chat(): String = "Good morning!"
    }

    private class NormalLottoVendingMachine : MachineLottoSeller() {
        override val lottoPrice: Int = 1_000

        override fun reset(): String = "Reset quietly"
    }

    private class NoisyLottoVendingMachine : MachineLottoSeller() {
        override val lottoPrice: Int = 1_000

        override fun reset(): String = "Reset with noise"
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
