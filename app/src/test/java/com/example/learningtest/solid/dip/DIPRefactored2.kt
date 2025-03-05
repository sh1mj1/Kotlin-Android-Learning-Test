package com.example.learningtest.solid.dip

class DIPRefactored2 {
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

    class RestActions(
        private val restActions: List<RestAction>,
    ) {
        constructor(vararg restActions: RestAction) : this(restActions.toList())

        fun onRest(): String = restActions.joinToString("\n") { it.onRest() }
    }

    abstract class LottoSeller(
        val restActions: RestActions,
    ) {
        abstract val lottoPrice: Int

        private var _restRequired: Boolean = true
        val restRequired: Boolean
            get() = _restRequired

        fun soldLotto(
            money: Int,
            lottoGenerateStrategies: List<LottoGenerateStrategy>,
        ): List<Lottery> {
            val count = money / lottoPrice
            require(count == lottoGenerateStrategies.size) { "the number of lottoGenerateStrategies must be same with lotto count" }

            return lottoGenerateStrategies.map(LottoGenerateStrategy::lotto)
        }

        fun onRest(): String = restActions.onRest()
    }

    class NormalLottoSeller(
        restActions: RestActions = RestActions(ChatCapable(message = "Chatting -- Hello!")),
    ) : LottoSeller(restActions) {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller(
        restActions: RestActions = RestActions(ChatCapable(message = "Chatting -- Good morning!")),
    ) : LottoSeller(restActions) {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    class NormalLottoVendingMachine(
        restActions: RestActions = RestActions(ResetCapable(message = "Resetting -- Quietly")),
    ) : LottoSeller(restActions) {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class NoisyLottoVendingMachine(
        restActions: RestActions = RestActions(ResetCapable(message = "Resetting -- With noise")),
    ) : LottoSeller(restActions) {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class ChatbotLottoSeller(
        restActions: RestActions =
            RestActions(
                ChatCapable(message = "Chatting -- Hello, You can call me chatbot!"),
                ResetCapable(message = "Resetting -- Quietly"),
            ),
    ) : LottoSeller(restActions) {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    data class Lottery(
        val numbers: List<Int>,
        val shape: Shape,
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

    data class Rectangle(private var width: Int, private var height: Int) : Shape {
        fun setWidth(width: Int) {
            this.width = width
        }

        fun setHeight(height: Int) {
            this.height = height
        }

        override fun area() = width * height
    }

    data class Square(private var side: Int) : Shape {
        fun setSide(side: Int) {
            this.side = side
        }

        override fun area() = side * side
    }

    class RandomSquareLottoGenerateStrategy() : LottoGenerateStrategy {
        override fun lotto(): Lottery =
            Lottery(
                numbers = (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
                Square(3),
            )
    }

    interface LottoGenerateStrategy {
        fun lotto(): Lottery
    }

    class RandomLottoGenerateStrategy(
        private val shape: Shape,
    ) : LottoGenerateStrategy {
        override fun lotto(): Lottery =
            Lottery(
                numbers = (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
                shape,
            )
    }

    class ManualLottoGenerateStrategy(
        val numbers: List<Int>,
        val shape: Shape,
    ) : LottoGenerateStrategy {
        override fun lotto(): Lottery =
            Lottery(
                numbers = numbers,
                shape = shape,
            )
    }
}
