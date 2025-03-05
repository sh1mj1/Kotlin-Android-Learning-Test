package com.example.learningtest.solid.dip

class DIPRefactored1 {
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

    class ChatCapable(private val message: String) : RestAction {
        override fun onRest(): String = chat()

        fun chat(): String = message
    }

    class ResetCapable(private val message: String) : RestAction {
        override fun onRest(): String = reset()

        fun reset(): String = message
    }

    class RestActions(
        private val restActions: List<RestAction>,
    ) {
        constructor(vararg restActions: RestAction) : this(restActions.toList())

        fun onRest(): String = restActions.joinToString("\n") { it.onRest() }
    }

    abstract class LottoSeller {
        abstract val lottoPrice: Int
        abstract val restActions: RestActions

        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        fun soldLotto(money: Int): List<Lottery> {
            val count = money / lottoPrice
            return List(count) { RandomSquareLottoGenerateStrategy().lotto() }
        }

        fun onRest(): String = restActions.onRest()
    }

    class NormalLottoSeller() : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restActions: RestActions =
            RestActions(
                ChatCapable(message = "Chatting -- Hello!"),
            )

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restActions: RestActions =
            RestActions(
                ChatCapable(message = "Chatting -- Good morning!"),
            )

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    class NormalLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restActions: RestActions =
            RestActions(
                ResetCapable(message = "Resetting -- Quietly"),
            )

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class NoisyLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restActions: RestActions =
            RestActions(
                ResetCapable(message = "Resetting -- With noise"),
            )

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class ChatbotLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE
        override val restActions: RestActions =
            RestActions(
                ChatCapable(message = "Chatting -- Hello, You can call me chatbot!"),
                ResetCapable(message = "Resetting -- Quietly"),
            )

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
