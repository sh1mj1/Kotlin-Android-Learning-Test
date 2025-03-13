package solid.an.mvp

import com.example.learningtest.solid.an.LottoContract
import lottery.pure.LottoSellerResult.Failure.InsufficientFunds
import lottery.pure.LottoSellerResult.Failure.InvalidAmount
import lottery.pure.LottoSellerResult.Success
import lottery.pure.ManualLottoGenerateStrategy
import lottery.pure.RandomLottoGenerateStrategy
import lottery.pure.Rectangle
import lottery.pure.ShapeResult
import lottery.pure.Square
import solid.an.R

class LottoPresenter(private val view: LottoContract.View) : LottoContract.Presenter {
    private var selectedSeller: lottery.pure.LottoSeller? = null
    private var selectedShape: lottery.pure.Shape? = null
    private var lottoType: String? = null
    private var money: Int? = null
    private var manualNumbers: List<Int>? = null

    override fun updateSeller(sellerId: Int) {
        selectedSeller =
            when (sellerId) {
                R.id.radioNormalSeller -> lottery.pure.NormalLottoSeller()
                R.id.radioDiscountSeller -> lottery.pure.DisCountLottoSeller()
                R.id.radioVendingNormal -> lottery.pure.NormalLottoVendingMachine()
                R.id.radioVendingNoisy -> lottery.pure.NoisyLottoVendingMachine()
                else -> null
            }
    }

    override fun updateShape(shapeId: Int) {
        when (shapeId) {
            R.id.radioRectangle ->
                view.setShapeInputsVisibility(
                    showWidth = true,
                    showHeight = true,
                )

            R.id.radioSquare -> view.setShapeInputsVisibility(showWidth = true, showHeight = false)
            else -> view.setShapeInputsVisibility(showWidth = false, showHeight = false)
        }
        selectedShape = null
    }

    override fun updateLottoType(typeId: Int) {
        lottoType =
            when (typeId) {
                R.id.radioAuto -> "AUTO"
                R.id.radioManual -> "MANUAL"
                else -> null
            }
        view.setManualNumbersVisibility(typeId == R.id.radioManual)
    }

    override fun updateMoney() {
        money = view.getMoneyInput().toIntOrNull()
    }

    override fun updateManualNumbers() {
        manualNumbers =
            view.getManualNumbersInput()?.split(",")?.mapNotNull { it.trim().toIntOrNull() }
    }

    override fun buyLotto() {
        runCatching {
            val seller = selectedSeller ?: error("로또 판매자를 선택해주세요.")
            val amount = money ?: error("구매할 금액을 입력해주세요.")

            when (val result = seller.lottoCount(amount)) {
                is InsufficientFunds ->
                    error(
                        "금액이 부족합니다. 최소 ${result.requiredAmount}원 이상 입력하세요.",
                    )

                is InvalidAmount ->
                    error(
                        "금액은 ${result.requiredUnit}원 단위로 입력해야 합니다.",
                    )

                is Success -> {}
            }

            val shape =
                when (view.getWidthInput().toIntOrNull()) {
                    null -> error("로또 모양을 선택해주세요.")
                    else ->
                        when (view.getHeightInput().toIntOrNull()) {
                            null ->
                                Square.create(
                                    view.getWidthInput().toInt(),
                                )

                            else ->
                                Rectangle.create(
                                    view.getWidthInput().toInt(),
                                    view.getHeightInput().toInt(),
                                )
                        }
                }

            val selectedShape =
                when (shape) {
                    is ShapeResult.Success -> shape.value
                    is ShapeResult.Failure.InvalidShapeSize -> error("모양의 길이가 올바르지 않습니다.")
                }

            val type = lottoType ?: error("로또 구매 방식을 선택해주세요.")
            if (type == "MANUAL") {
                val numbers = manualNumbers ?: error("수동 로또 번호를 입력해주세요.")
                require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
            }

            val numbers =
                manualNumbers?.let {
                    require(it.size == 6) { "6개의 숫자를 입력해주세요." }
                    it
                } ?: error("수동 로또 번호를 입력해주세요.")

            val count = amount / seller.lottoPrice
            val lottoes =
                List(count) {
                    when (type) {
                        "AUTO" -> RandomLottoGenerateStrategy(selectedShape).lotto()
                        "MANUAL" ->
                            ManualLottoGenerateStrategy(numbers, selectedShape).lotto()

                        else -> error("잘못된 로또 구매 방식입니다.")
                    }
                }

            lottoes.joinToString("\n") {
                "Numbers: ${it.numbers} " +
                    "(${
                        when (selectedShape) {
                            is Rectangle -> "가로: ${selectedShape.width}, 세로: ${selectedShape.height}"
                            is Square -> "한 변의 길이: ${selectedShape.side}"
                        }
                    })"
            }
        }
            .onSuccess { result -> view.showResult(result) }
            .onFailure { error -> view.showError(error.message ?: "알 수 없는 오류") }
    }
}
