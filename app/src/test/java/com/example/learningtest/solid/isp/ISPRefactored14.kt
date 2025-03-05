package com.example.learningtest.solid.isp

class ISPRefactored14 {
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

    sealed class LottoSeller {
        abstract val lottoPrice: Int
        abstract val restAction: RestAction

        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

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
