package com.example.learningtest.solid.ocp

class OCPViolatedIf {
    class Customer {
        fun buyLotto(
            money: Int,
            lottoSeller: LottoSeller,
            lottoSellerType: LottoSellerType,
        ): List<Lottery> = lottoSeller.soldLotto(money, lottoSellerType)
    }

    class LottoSeller {
        fun soldLotto(
            money: Int,
            lottoSellerType: LottoSellerType,
        ): List<Lottery> {
            val count =
                when (lottoSellerType) {
                    LottoSellerType.NORMAL -> money / LOTTO_PRICE
                    LottoSellerType.DISCOUNT -> money / DISCOUNT_LOTTO_PRICE
                    LottoSellerType.PREMIUM -> money / PREMIUM_LOTTO_PRICE
                }
            return List(count) { LotteryGenerateStrategy().lotto() }
        }

        companion object {
            private const val LOTTO_PRICE = 1000
            private const val DISCOUNT_LOTTO_PRICE = 500
            private const val PREMIUM_LOTTO_PRICE = 2000
        }
    }

    enum class LottoSellerType {
        NORMAL,
        DISCOUNT,
        PREMIUM,
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
