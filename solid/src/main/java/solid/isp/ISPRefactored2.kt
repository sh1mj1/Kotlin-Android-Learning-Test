package solid.isp

import kotlin.collections.forEach
import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take

/**
 * LSP is satisfied.
 *
 * Introduced the RestAction interface to separate rest-related actions from LottoSeller.
 * However, ChatbotLottoSeller manually declares the chat and reset methods.
 */
class ISPRefactored2 {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> {
            if (lottoSeller.restRequired) println(lottoSeller.onRest())

            return lottoSeller.soldLotto(money)
        }
    }

    sealed interface RestAction {
        fun onRest(): String
    }

    sealed class LottoSeller : RestAction {
        abstract val lottoPrice: Int

        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        fun soldLotto(money: Int): List<Lottery> {
            val count = money / lottoPrice
            return List(count) { RandomSquareLottoGenerateStrategy().lotto() }
        }
    }

    abstract class HumanLottoSeller : LottoSeller() {
        abstract fun chat(): String

        override fun onRest(): String = chat()
    }

    abstract class LottoVendingMachine : LottoSeller() {
        abstract fun reset(): String

        override fun onRest(): String = reset()
    }

    abstract class ChatbotLottoSeller : LottoSeller() {
        abstract fun chat(): String

        abstract fun reset(): String

        override fun onRest(): String = chat() + '\n' + reset()
    }

    class DefaultChatbotSeller : ChatbotLottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = "Chatting -- Hello, You can call me chatbot!"

        override fun reset(): String = "Resetting -- Quietly"

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class NormalLottoSeller : HumanLottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = "Chatting -- Hello!"

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller : HumanLottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun chat(): String = "Chatting -- Good morning!"

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    class NormalLottoVendingMachine : LottoVendingMachine() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun reset(): String = "Resetting -- Quietly"

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class NoisyLottoVendingMachine : LottoVendingMachine() {
        override val lottoPrice: Int = LOTTO_PRICE

        override fun reset(): String = "Resetting -- With noise"

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
