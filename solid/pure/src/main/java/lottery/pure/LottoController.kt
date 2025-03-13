package lottery.pure

import kotlin.collections.joinToString
import kotlin.collections.map

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
        return when (val result = lottoSeller.lottoCount(paidMoney)) {
            is LottoSellerResult.Success -> result.value
            is LottoSellerResult.Failure -> {
                when (result) {
                    is LottoSellerResult.Failure.InsufficientFunds ->
                        view.displayError(
                            "금액을 ${lottoSeller.lottoPrice}원 이상 입력해주세요.",
                        )

                    is LottoSellerResult.Failure.InvalidAmount ->
                        view.displayError(
                            "금액을 ${lottoSeller.lottoPrice}원 단위로 입력해주세요.",
                        )
                }
                lottoCount(lottoSeller)
            }
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

        val shapeResult =
            when (shapeType) {
                LottoShape.RECTANGLE -> Rectangle.create(dimensions.length[0], dimensions.length[1])
                LottoShape.SQUARE -> Square.create(dimensions.length[0])
            }

        return when (shapeResult) {
            is ShapeResult.Success ->
                when (lottoInput) {
                    is AutoLottoInput -> RandomLottoGenerateStrategy(shapeResult.value)
                    is ManualLottoInput ->
                        ManualLottoGenerateStrategy(
                            lottoInput.numbers,
                            shapeResult.value,
                        )
                }

            ShapeResult.Failure.InvalidShapeSize -> {
                view.displayError("잘못된 도형 크기입니다.")
                throw IllegalArgumentException("잘못된 도형 크기입니다.")
            }
        }
    }
}
