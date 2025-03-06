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

    fun lottoCount(lottoSeller: LottoSeller): Int = lottoSeller.lottoCount(view.paidMoney())

    fun lottoGenerateStrategies(count: Int): List<LottoGenerateStrategy> {
        val lottoInputs = view.lottoInputs(count)
        return lottoInputs.map { lottoInput ->
            lottoGenerateStrategy(lottoInput.strategy, lottoInput.shape, lottoInput.manualNumbers)
        }
    }

    fun boughtLotto(
        count: Int,
        lottoSeller: LottoSeller,
        lottoGenerateStrategies: List<LottoGenerateStrategy>,
    ) {
        require(count == lottoGenerateStrategies.size) { "the number of lottoGenerateStrategies must be same with lotto count" }

        if (lottoSeller.restRequired) {
            view.displayRestMessage(lottoSeller.onRest())
        }

        val lottoNumbers =
            lottoSeller.soldLotto(count, lottoGenerateStrategies)
                .map { lotto ->
                    val shapeType = if (lotto.shape is Rectangle) "사각형" else "정사각형"
                    val numbers =
                        if (lotto.numbers.isEmpty()) "수동 입력 오류" else lotto.numbers.joinToString(", ")
                    numbers to shapeType
                }

        view.displayLottoNumbers(lottoNumbers)
    }

    private fun lottoGenerateStrategy(
        strategy: LottoStrategy,
        shapeInfo: LottoShapeInput,
        manualNumbers: List<Int>?,
    ): LottoGenerateStrategy {
        val (shapeType, dimensions) = shapeInfo

        val shape =
            when (shapeType) {
                LottoShape.RECTANGLE -> Rectangle(dimensions.length[0], dimensions.length[1])
                LottoShape.SQUARE -> Square(dimensions.length[0])
            }
        return when (strategy) {
            LottoStrategy.AUTO -> RandomLottoGenerateStrategy(shape)
            LottoStrategy.MANUAL -> {
                requireNotNull(manualNumbers) { "수동 로또는 반드시 번호를 입력해야 합니다." }
                ManualLottoGenerateStrategy(manualNumbers, shape)
            }
        }
    }
}
