package solid.isp

import kotlin.collections.forEach
import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take

/**
 * ISP is violated because the `LottoSeller` interface has too many methods.
 */
class ISPViolated {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> = lottoSeller.soldLotto(money)
    }

    sealed class LottoSeller {
        abstract val lottoPrice: Int

        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        abstract fun chat(): String

        abstract fun reset(): String

        fun soldLotto(money: Int): List<Lottery> {
            if (restRequired) println(recoverMessage())
            val count = money / lottoPrice
            return List(count) { RandomSquareLottoGenerateStrategy().lotto() }
        }

        private fun recoverMessage(): String =
            when (this) {
                is NormalLottoSeller -> chat()
                is DisCountLottoSeller -> chat()
                is NormalLottoVendingMachine -> reset()
                is NoisyLottoVendingMachine -> reset()
            }
    }

    class NormalLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = "Hello!"

        override fun reset(): String = error("Human is not machine, can't not be reset")

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = "Good morning!"

        override fun reset(): String = error("Human is not machine, can't not be reset")

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    class NormalLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = error("This is Machine, can't chat")

        override fun reset(): String = "Reset quietly"

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class NoisyLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = error("This is Machine, can't chat")

        override fun reset(): String = "Reset with noise"

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    data class Lottery(
        val numbers: List<Int>,
        val rectangle: Shape,
    ) {
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

    interface Shape {
        fun area(): Int
    }

    class Rectangle(private var width: Int, private var height: Int) : Shape {
        fun setWidth(width: Int) {
            this.width = width
        }

        fun setHeight(height: Int) {
            this.height = height
        }

        override fun area() = width * height
    }

    class Square(private var side: Int) : Shape {
        fun setSide(side: Int) {
            this.side = side
        }

        override fun area() = side * side
    }

    class RandomSquareLottoGenerateStrategy {
        fun lotto(): Lottery =
            Lottery(
                numbers = (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
                Square(3),
            )
    }
}
