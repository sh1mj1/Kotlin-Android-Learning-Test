package com.example.learningtest.solid.lsp

class LSPViolated1 {
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
            return List(count) { RandomSquareLottoGenerateStrategy().lotto() }
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

    data class Lottery(
        val numbers: List<Int>,
        val rectangle: Rectangle = Square(3, 3),
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

    open class Rectangle(
        private var _height: Int,
        private var _width: Int,
    ) {
        val height: Int
            get() = _height
        val width: Int
            get() = _width

        open fun changeHeight(height: Int) {
            this._height = height
        }

        open fun changeWidth(width: Int) {
            this._width = width
        }

        fun area(): Int = _height * _width
    }

    class Square(
        height: Int,
        width: Int,
    ) : Rectangle(height, width) {
        override fun changeHeight(height: Int) {
            setSide(height)
        }

        override fun changeWidth(width: Int) {
            setSide(width)
        }

        private fun setSide(side: Int) {
            super.changeHeight(side)
            super.changeWidth(side)
        }
    }

    class RandomSquareLottoGenerateStrategy {
        fun lotto(): Lottery =
            Lottery(
                numbers = (Lottery.numberRange).shuffled().take(Lottery.NUMBER_COUNT).sorted(),
                Square(3, 3),
            )
    }
}
