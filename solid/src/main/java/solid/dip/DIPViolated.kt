package solid.dip

import kotlin.collections.forEach
import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take

/**
 * DIP is violated.
 *
 * LottoSeller has a dependency on Random Square Lotto Generate Strategy.
 */
class DIPViolated {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> {
            if (lottoSeller.restRequired) println(lottoSeller.onRest())
            return lottoSeller.soldLotto(money)
        }
    }

    interface RestAction {
        fun onRest(): String
    }

    interface ChatCapable : RestAction {
        fun chat(): String
    }

    interface ResetCapable : RestAction {
        fun reset(): String
    }

    interface ChatResetCapable : ChatCapable, ResetCapable

    class MorningChat : ChatCapable {
        override fun onRest(): String = chat()

        override fun chat(): String = "Chatting -- Good morning!"
    }

    class HelloChat : ChatCapable {
        override fun onRest(): String = chat()

        override fun chat(): String = "Chatting -- Hello!"
    }

    class QuietReset : ResetCapable {
        override fun onRest(): String = reset()

        override fun reset(): String = "Resetting -- Quietly"
    }

    class NoisyReset : ResetCapable {
        override fun onRest(): String = reset()

        override fun reset(): String = "Resetting -- With noise"
    }

    class DefaultChatResetCapable : ChatResetCapable {
        override fun chat(): String = "Chatting -- Hello, You can call me chatbot!"

        override fun reset(): String = "Resetting -- Quietly"

        override fun onRest(): String = chat() + '\n' + reset()
    }

    abstract class LottoSeller {
        private var _restRequired: Boolean = true
        abstract val restAction: RestAction

        val restRequired: Boolean
            get() = _restRequired

        abstract val lottoPrice: Int

        fun soldLotto(money: Int): List<Lottery> {
            val count = money / lottoPrice
            return List(count) { RandomSquareLottoGenerateStrategy().lotto() }
        }

        fun onRest(): String = restAction.onRest()
    }

    class NormalLottoSeller() : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restAction: RestAction = HelloChat()

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restAction: RestAction = MorningChat()

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    class NormalLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restAction: RestAction = QuietReset()

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class NoisyLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restAction: RestAction = NoisyReset()

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class ChatbotLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restAction: RestAction = DefaultChatResetCapable()

        companion object {
            private const val LOTTO_PRICE = 1000
        }
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

    class RandomSquareLottoGenerateStrategy {
        fun lotto(): Lottery =
            Lottery(
                numbers = (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
                Square(3),
            )
    }
}
