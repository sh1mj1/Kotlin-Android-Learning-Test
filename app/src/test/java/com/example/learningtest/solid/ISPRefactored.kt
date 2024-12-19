package com.example.learningtest.solid

class ISPRefactored {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> {
            when (lottoSeller) {
                is HumanLottoSeller -> println(lottoSeller.chat())
                is MachineLottoSeller -> println(lottoSeller.reset())
            }
            return lottoSeller.soldLotto(money)
        }
    }

    abstract class LottoSeller {
        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        abstract val lottoPrice: Int

        abstract fun soldLotto(money: Int): List<Lottery>
    }

    abstract class HumanLottoSeller : LottoSeller() {
        override fun soldLotto(money: Int): List<Lottery> = List(money / lottoPrice) { LotteryGenerateStrategy().autoGenerate() }

        abstract fun chat(): String
    }

    abstract class MachineLottoSeller : LottoSeller() {
        override fun soldLotto(money: Int): List<Lottery> = List(money / lottoPrice) { LotteryGenerateStrategy().autoGenerate() }

        abstract fun reset(): String
    }

    class NormalLottoSeller : HumanLottoSeller() {
        override val lottoPrice: Int = 1_000

        override fun chat(): String = "Hello!"
    }

    class DiscountedLottoSeller : HumanLottoSeller() {
        override val lottoPrice: Int = 500

        override fun chat(): String = "Good morning!"
    }

    class NormalLottoVendingMachine : MachineLottoSeller() {
        override val lottoPrice: Int = 1_000

        override fun reset(): String = "Reset quietly"
    }

    class NoisyLottoVendingMachine : MachineLottoSeller() {
        override val lottoPrice: Int = 1_000

        override fun reset(): String = "Reset with noise"
    }

    data class Lottery(val numbers: List<Int>, val shape: Shape = Square()) {
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

    interface Shape {
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
