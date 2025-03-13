package solid.dip

import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take
import kotlin.collections.toList

/**
 * LottoSeller does not directly depend on LottoGenerateStrategy
 *
 * But unnecessary DI causes Dependency Hell problem.
 */
class DIPRefactored1 {
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
        override val lottoPrice: Int,
        restActions: RestActions,
    ) : LottoSeller(restActions)

    class DisCountLottoSeller(
        override val lottoPrice: Int,
        restActions: RestActions,
    ) : LottoSeller(restActions)

    class NormalLottoVendingMachine(
        override val lottoPrice: Int,
        restActions: RestActions,
    ) : LottoSeller(restActions)

    class NoisyLottoVendingMachine(
        override val lottoPrice: Int,
        restActions: RestActions,
    ) : LottoSeller(restActions)

    class ChatbotLottoSeller(
        override val lottoPrice: Int,
        restActions: RestActions,
    ) : LottoSeller(restActions)

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
        fun changeWidth(width: Int) {
            this.width = width
        }

        fun changeHeight(height: Int) {
            this.height = height
        }

        override fun area() = width * height
    }

    data class Square(private var side: Int) : Shape {
        fun changeSide(side: Int) {
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
