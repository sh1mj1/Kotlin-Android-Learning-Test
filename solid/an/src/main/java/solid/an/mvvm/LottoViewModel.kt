package solid.an.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lottery.pure.DisCountLottoSeller
import lottery.pure.LottoSeller
import lottery.pure.LottoSellerResult
import lottery.pure.ManualLottoGenerateStrategy
import lottery.pure.NoisyLottoVendingMachine
import lottery.pure.NormalLottoSeller
import lottery.pure.NormalLottoVendingMachine
import lottery.pure.RandomLottoGenerateStrategy
import lottery.pure.Rectangle
import lottery.pure.Shape
import lottery.pure.ShapeResult
import lottery.pure.Square
import solid.an.R

class LottoViewModel : ViewModel() {
    val selectedSeller = MutableLiveData<LottoSeller?>()
    val selectedShape = MutableLiveData<Shape?>()
    val lottoType = MutableLiveData<String?>()
    val manualNumbers = MutableLiveData("")

    val money = MutableLiveData("")
    val width = MutableLiveData("")
    val height = MutableLiveData("")

    private val _result = MutableLiveData("")
    val result: LiveData<String> get() = _result

    private val _error = MutableLiveData("")
    val error: LiveData<String> get() = _error

    val showManualNumbers = MutableLiveData(false)
    val showWidth = MutableLiveData(false)
    val showHeight = MutableLiveData(false)

    private val _showError = MutableLiveData(false)
    val showError: LiveData<Boolean> get() = _showError

    fun updateSeller(sellerId: Int) {
        selectedSeller.value =
            when (sellerId) {
                R.id.radioNormalSeller -> NormalLottoSeller()
                R.id.radioDiscountSeller -> DisCountLottoSeller()
                R.id.radioVendingNormal -> NormalLottoVendingMachine()
                R.id.radioVendingNoisy -> NoisyLottoVendingMachine()
                else -> null
            }
    }

    fun updateShape(shapeId: Int) {
        when (shapeId) {
            R.id.radioRectangle -> {
                showWidth.value = true
                showHeight.value = true
            }

            R.id.radioSquare -> {
                showWidth.value = true
                showHeight.value = false
            }

            else -> {
                showWidth.value = false
                showHeight.value = false
            }
        }
        selectedShape.value = null
    }

    fun updateLottoType(typeId: Int) {
        lottoType.value =
            when (typeId) {
                R.id.radioAuto -> "AUTO"
                R.id.radioManual -> "MANUAL"
                else -> null
            }
        showManualNumbers.value = (typeId == R.id.radioManual)
    }

    fun buyLotto() {
        runCatching {
            val seller = selectedSeller.value ?: error("로또 판매자를 선택해주세요.")
            val amount = money.value?.toIntOrNull() ?: error("구매할 금액을 입력해주세요.")

            when (val result = seller.lottoCount(amount)) {
                is LottoSellerResult.Failure.InsufficientFunds ->
                    error(
                        "금액이 부족합니다. 최소 ${result.requiredAmount}원 이상 입력하세요.",
                    )

                is LottoSellerResult.Failure.InvalidAmount ->
                    error(
                        "금액은 ${result.requiredUnit}원 단위로 입력해야 합니다.",
                    )

                is LottoSellerResult.Success -> {}
            }

            val shape =
                when (width.value?.toIntOrNull()) {
                    null -> error("로또 모양을 선택해주세요.")
                    else ->
                        when (height.value?.toIntOrNull()) {
                            null -> Square.Companion.create(width.value!!.toInt())
                            else ->
                                Rectangle.Companion.create(
                                    width.value!!.toInt(),
                                    height.value!!.toInt(),
                                )
                        }
                }

            val selectedShape =
                when (shape) {
                    is ShapeResult.Success -> shape.value
                    is ShapeResult.Failure.InvalidShapeSize -> error("모양의 길이가 올바르지 않습니다.")
                }

            val type = lottoType.value ?: error("로또 구매 방식을 선택해주세요.")
            if (type == "MANUAL") {
                val numbers =
                    manualNumbers.value?.split(",")?.mapNotNull { it.trim().toIntOrNull() }
                        ?: error("수동 로또 번호를 입력해주세요.")
                require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
            }

            val count = amount / seller.lottoPrice

            val lottoNumbers =
                manualNumbers.value.let {
                    requireNotNull(it) { "수동 로또 번호를 입력해주세요." }
                    val numbers = it.trim().split(",").map { it.trim().toInt() }
                    require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
                    numbers
                }

            val lottoes =
                List(count) {
                    when (type) {
                        "AUTO" -> RandomLottoGenerateStrategy(selectedShape).lotto()
                        "MANUAL" ->
                            ManualLottoGenerateStrategy(lottoNumbers, selectedShape).lotto()

                        else -> error("잘못된 로또 구매 방식입니다.")
                    }
                }

            _result.value =
                lottoes.joinToString("\n") {
                    "Numbers: ${it.numbers} " +
                        "(${
                            when (selectedShape) {
                                is Rectangle -> "가로: ${selectedShape.width}, 세로: ${selectedShape.height}"
                                is Square -> "한 변의 길이: ${selectedShape.side}"
                            }
                        })"
                }
            _error.value = ""
            _showError.value = false
        }.onFailure {
            _error.value = "⚠️ 오류: ${it.message}"
            _showError.value = true
        }
    }
}
