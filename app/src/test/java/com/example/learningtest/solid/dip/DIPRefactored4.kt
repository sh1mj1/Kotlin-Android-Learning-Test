package com.example.learningtest.solid.dip

class DIPRefactored4 {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
            lottoGenerateStrategies: List<LottoGenerateStrategy>,
        ): List<Lottery> {
            if (lottoSeller.restRequired) println(lottoSeller.onRest())

            return lottoSeller.soldLotto(money, lottoGenerateStrategies)
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

    class RestActions(private val restActions: List<RestAction>) {
        constructor(vararg restActions: RestAction) : this(restActions.toList())

        fun onRest(): String = restActions.joinToString("\n") { it.onRest() }
    }

    abstract class LottoSeller {
        abstract val lottoPrice: Int
        open val restActions: RestActions = RestActions()

        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        fun soldLotto(
            money: Int,
            lottoGenerateStrategies: List<LottoGenerateStrategy>,
        ): List<Lottery> {
            val count = money / lottoPrice
            require(count == lottoGenerateStrategies.size) { "the number of lottoGenerateStrategies must be same with lotto count" }

            return lottoGenerateStrategies.map { it.lotto() }
        }

        fun onRest(): String = restActions.onRest()
    }

    class NormalLottoSeller : LottoSeller() {
        override val lottoPrice: Int = 1_000
        override val restActions: RestActions =
            RestActions(ChatCapable(message = "Chatting -- Hello!"))
    }

    class DisCountLottoSeller : LottoSeller() {
        override val lottoPrice: Int = 500
        override val restActions: RestActions =
            RestActions(ChatCapable(message = "Chatting -- Good morning!"))
    }

    class NormalLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = 1_000
        override val restActions: RestActions =
            RestActions(ResetCapable(message = "Resetting -- Quietly"))
    }

    class NoisyLottoVendingMachine : LottoSeller() {
        override val lottoPrice: Int = 1_000
        override val restActions: RestActions =
            RestActions(ResetCapable(message = "Resetting -- With noise"))
    }

    class ChatbotLottoSeller : LottoSeller() {
        override val lottoPrice: Int = 1_000
        override val restActions: RestActions =
            RestActions(
                ChatCapable(message = "Chatting -- Hello, You can call me chatbot!"),
                ResetCapable(message = "Resetting -- Quietly"),
            )
    }

    data class Lottery(val numbers: List<Int>, val shape: Shape) {
        init {
            require(numbers.size == NUMBER_COUNT) { "Invalid lotto number count" }
            numbers.forEach {
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

    data class Rectangle(private var width: Int, private var height: Int) : Shape {
        override fun area() = width * height
    }

    data class Square(private var side: Int) : Shape {
        override fun area() = side * side
    }

    interface LottoGenerateStrategy {
        fun lotto(): Lottery
    }

    class ManualLottoGenerateStrategy(
        val numbers: List<Int>,
        val shape: Shape,
    ) : LottoGenerateStrategy {
        override fun lotto(): Lottery = Lottery(numbers = numbers, shape = shape)
    }
}
