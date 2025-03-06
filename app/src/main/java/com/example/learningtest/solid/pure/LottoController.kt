package com.example.learningtest.solid.pure

/**
 * Controller - 흐름 제어
 */
class LottoController(private val view: CustomerView) {
    fun lottoSeller(): LottoSeller {
        val sellerChoice = view.selectedLottoSeller()
        return when (sellerChoice) {
            LottoSellerType.NORMAL -> NormalLottoSeller()
            LottoSellerType.DISCOUNT -> DisCountLottoSeller()
            LottoSellerType.NORMAL_MACHINE -> NormalLottoVendingMachine()
            LottoSellerType.NOISY_MACHINE -> NoisyLottoVendingMachine()
        }
    }

    fun lottoCount(lottoSeller: LottoSeller): Int {
        val paidMoney = view.paidMoney()
        return try {
            val lottoCount = lottoSeller.lottoCount(paidMoney)
            lottoCount
        } catch (e: IllegalArgumentException) {
            when (e.message) {
                "Too small money" -> view.displayError("금액을 ${lottoSeller.lottoPrice}원 이상 입력해주세요.")

                "money is not exactly divisible by lotto price." ->
                    view.displayError("금액을 ${lottoSeller.lottoPrice}원 단위로 입력해주세요.")
            }
            lottoCount(lottoSeller)
        }
    }

    fun lottoGenerateStrategies(count: Int): List<LottoGenerateStrategy> {
        retryInput {
            val lottoInputs = view.lottoInputs(count)
            return lottoInputs.map { lottoInput ->
                lottoGenerateStrategy(lottoInput)
            }
        }
    }

    fun boughtLotto(
        count: Int,
        lottoSeller: LottoSeller,
        lottoGenerateStrategies: List<LottoGenerateStrategy>,
    ) {
        require(count == lottoGenerateStrategies.size) { "not same lotto and strategies count" }

        if (lottoSeller.restRequired) {
            view.displayRest(lottoSeller.onRest())
        }

        val lottoNumbers =
            lottoSeller.soldLotto(lottoGenerateStrategies)
                .map { lotto ->

                    val shape =
                        when (lotto.shape) {
                            is Rectangle -> LottoShape.RECTANGLE
                            is Square -> LottoShape.SQUARE
                        }

                    val numbers =
                        if (lotto.numbers.isEmpty()) "수동 입력 오류" else lotto.numbers.joinToString(", ")
                    LottoOutput(shape = shape, numbers = numbers)
                }

        view.displayLottoNumbers(lottoNumbers)
    }

    private fun lottoGenerateStrategy(lottoInput: LottoInput): LottoGenerateStrategy {
        val (shapeType, dimensions) = lottoInput.shape

        val shape =
            try {
                when (shapeType) {
                    LottoShape.RECTANGLE -> Rectangle(dimensions.length[0], dimensions.length[1])
                    LottoShape.SQUARE -> Square(dimensions.length[0])
                }
            } catch (e: IllegalArgumentException) {
                when (e.message) {
                    "Invalid width or height" -> throw IllegalArgumentException("가로와 세로 길이는 1 이상이어야 합니다.")
                    "Invalid side" -> throw IllegalArgumentException("한 변의 길이는 1 이상이어야 합니다.")
                    else -> error("")
                }
            }

        return when (lottoInput) {
            is AutoLottoInput -> RandomLottoGenerateStrategy(shape)
            is ManualLottoInput -> ManualLottoGenerateStrategy(lottoInput.numbers, shape)
        }
    }
}
