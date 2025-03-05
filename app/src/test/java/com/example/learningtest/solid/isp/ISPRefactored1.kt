    package com.example.learningtest.solid.isp

    class ISPRefactored1 {
        class Customer {
            fun buyLotto(
                money: Int,
                lottoSeller: LottoSeller,
            ): List<Lottery> {
                if (lottoSeller.restRequired) println(lottoSeller.recoverMessage())

                return lottoSeller.soldLotto(money)
            }
        }

        sealed class LottoSeller {
            abstract val lottoPrice: Int

            private var _restRequired: Boolean = true
            val restRequired: Boolean
                get() = _restRequired

            fun soldLotto(money: Int): List<Lottery> {
                val count = money / lottoPrice
                return List(count) { RandomSquareLottoGenerateStrategy().lotto() }
            }

            fun recoverMessage(): String =
                when (this) {
                    is HumanLottoSeller -> chat()
                    is LottoVendingMachine -> reset()
                }
        }

        abstract class HumanLottoSeller : LottoSeller() {
            abstract fun chat(): String
        }

        abstract class LottoVendingMachine : LottoSeller() {
            abstract fun reset(): String
        }

        class NormalLottoSeller : HumanLottoSeller() {
            override val lottoPrice: Int = LOTTO_PRICE

            override fun chat(): String = "Hello!"

            companion object {
                private const val LOTTO_PRICE = 1000
            }
        }

        class DisCountLottoSeller : HumanLottoSeller() {
            override val lottoPrice: Int = LOTTO_PRICE

            override fun chat(): String = "Good morning!"

            companion object {
                private const val LOTTO_PRICE = 500
            }
        }

        class NormalLottoVendingMachine : LottoVendingMachine() {
            override val lottoPrice: Int = LOTTO_PRICE

            override fun reset(): String = "Reset quietly"

            companion object {
                private const val LOTTO_PRICE = 1000
            }
        }

        class NoisyLottoVendingMachine : LottoVendingMachine() {
            override val lottoPrice: Int = LOTTO_PRICE

            override fun reset(): String = "Reset with noise"

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
