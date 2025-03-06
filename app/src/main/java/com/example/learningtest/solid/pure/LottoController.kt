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
                "Pay an amount that is greater than or equal to the ticket price." -> {
                    view.displayError("금액을 ${lottoSeller.lottoPrice}원 이상 입력해주세요.")
                }

                "Pay an amount that is exactly divisible by the ticket price." -> {
                    view.displayError("금액을 ${lottoSeller.lottoPrice}원 단위로 입력해주세요.")
                }
            }
            lottoCount(lottoSeller)
        }
    }

    fun lottoGenerateStrategies(count: Int): List<LottoGenerateStrategy> {
        val lottoInputs = view.lottoInputs(count)
        return lottoInputs.map { lottoInput ->
            lottoGenerateStrategy(lottoInput)
        }
    }

    fun boughtLotto(
        count: Int,
        lottoSeller: LottoSeller,
        lottoGenerateStrategies: List<LottoGenerateStrategy>,
    ) {
        require(count == lottoGenerateStrategies.size) { "the number of lottoes and strategies must be same" }

        if (lottoSeller.restRequired) {
            view.displayRest(lottoSeller.onRest())
        }

        val lottoNumbers =
            lottoSeller.soldLotto(lottoGenerateStrategies)
                .map { lotto ->
                    val shapeType = if (lotto.shape is Rectangle) "사각형" else "정사각형"
                    val numbers =
                        if (lotto.numbers.isEmpty()) "수동 입력 오류" else lotto.numbers.joinToString(", ")
                    numbers to shapeType
                }

        view.displayLottoNumbers(lottoNumbers)
    }

    private fun lottoGenerateStrategy(lottoInput: LottoInput): LottoGenerateStrategy {
        val (shapeType, dimensions) = lottoInput.shape

        val shape =
            when (shapeType) {
                LottoShape.RECTANGLE -> Rectangle(dimensions.length[0], dimensions.length[1])
                LottoShape.SQUARE -> Square(dimensions.length[0])
            }

        return when (lottoInput) {
            is AutoLottoInput -> RandomLottoGenerateStrategy(shape)
            is ManualLottoInput -> ManualLottoGenerateStrategy(lottoInput.numbers, shape)
        }

//        return when (strategy) {
//            LottoStrategyMenu.AUTO -> RandomLottoGenerateStrategy(shape)
//            LottoStrategyMenu.MANUAL -> {
//                requireNotNull(manualNumbers) { "수동 로또는 반드시 번호를 입력해야 합니다." }
//                ManualLottoGenerateStrategy(manualNumbers, shape)
//            }
//        }
    }
}
