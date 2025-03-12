package solid.ocp

import kotlin.collections.forEach
import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take
import kotlin.collections.toSet

/**
 * OCP is satisfied, and template method pattern is applied.
 */
class OCPRefactored2 {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
        ): List<Lottery> = lottoSeller.soldLotto(money)
    }

    abstract class LottoSeller {
        abstract val lottoPrice: Int

        fun soldLotto(money: Int): List<Lottery> {
            val count = money / lottoPrice
            return List(count) { LotteryGenerateStrategy().lotto() }
        }
    }

    class NormalLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    class DisCountLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 500
        }
    }

    class PremiumLottoSeller : LottoSeller() {
        override val lottoPrice: Int = LOTTO_PRICE

        companion object {
            private const val LOTTO_PRICE = 2000
        }
    }

    data class Lottery(val numbers: List<Int>) {
        init {
            require(numbers.size == NUMBER_COUNT) {
                "Invalid lotto number count"
            }
            require(numbers.toSet().size == NUMBER_COUNT) {
                "Duplicate lotto number"
            }
            numbers.forEach {
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

    class LotteryGenerateStrategy {
        fun lotto(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            )
    }
}
