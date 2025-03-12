package solid.ocp

import kotlin.collections.forEach
import kotlin.collections.shuffled
import kotlin.collections.sorted
import kotlin.collections.take

/**
 * New Requirement: Normal Lotto Seller and Discount Lotto Seller
 *
 * OCP is violated, and SRP is also violated.
 */
class OCPViolated1 {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
            lottoSellerType: LottoSellerType,
        ): List<Lottery> =
            when (lottoSellerType) {
                LottoSellerType.NORMAL -> {
                    lottoSeller.soldLotto(money)
                }

                LottoSellerType.DISCOUNT -> {
                    val count = money / DISCOUNTED_LOTTO_PRICE
                    List(count) { LotteryGenerateStrategy().lotto() }
                }
            }

        companion object {
            private const val DISCOUNTED_LOTTO_PRICE = 500
        }
    }

    class LottoSeller {
        fun soldLotto(money: Int): List<Lottery> {
            val count = money / LOTTO_PRICE
            return List(count) { LotteryGenerateStrategy().lotto() }
        }

        companion object {
            private const val LOTTO_PRICE = 1000
        }
    }

    enum class LottoSellerType {
        NORMAL,
        DISCOUNT,
    }

    data class Lottery(val numbers: List<Int>) {
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

    class LotteryGenerateStrategy {
        fun lotto(): Lottery =
            Lottery(
                (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
            )
    }
}
