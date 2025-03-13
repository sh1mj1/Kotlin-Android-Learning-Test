package solid.an.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import lottery.pure.DisCountLottoSeller
import lottery.pure.LottoSellerResult
import lottery.pure.ManualLottoGenerateStrategy
import lottery.pure.NoisyLottoVendingMachine
import lottery.pure.NormalLottoSeller
import lottery.pure.NormalLottoVendingMachine
import lottery.pure.RandomLottoGenerateStrategy
import lottery.pure.Rectangle
import lottery.pure.ShapeResult
import lottery.pure.Square
import kotlin.collections.joinToString
import kotlin.onFailure
import kotlin.runCatching
import kotlin.text.toInt
import kotlin.text.toIntOrNull
import kotlin.text.trim

class LottoViewModel : ViewModel() {
    private val _state = MutableStateFlow(LottoState())
    val state: StateFlow<LottoState> get() = _state

    fun handleIntent(intent: LottoIntent) {
        viewModelScope.launch {
            when (intent) {
                is LottoIntent.SelectSeller -> updateSeller(intent.sellerId)
                is LottoIntent.SelectShape -> updateShape(intent.shapeId)
                is LottoIntent.SelectLottoType -> updateLottoType(intent.typeId)
                is LottoIntent.EnterMoney ->
                    _state.value =
                        LottoState(money = intent.amount)

                is LottoIntent.EnterWidth ->
                    _state.value =
                        LottoState(width = intent.width)

                is LottoIntent.EnterHeight ->
                    _state.value =
                        LottoState(height = intent.height)

                is LottoIntent.EnterManualNumbers ->
                    _state.value =
                        LottoState(manualNumbers = intent.numbers)

                is LottoIntent.BuyLotto -> buyLotto()
            }
        }
    }

    private fun updateSeller(sellerId: Int) {
        _state.value = LottoState(selectedSellerId = sellerId)
    }

    private fun updateShape(shapeId: Int) {
        val showWidth = shapeId == 1 || shapeId == 2
        val showHeight = shapeId == 1
        _state.value =
            LottoState(showWidth = showWidth, showHeight = showHeight)
    }

    private fun updateLottoType(typeId: Int) {
        val lottoType =
            when (typeId) {
                1 -> "AUTO"
                2 -> "MANUAL"
                else -> null
            }
        _state.value =
            LottoState(lottoType = lottoType, showManualNumbers = (lottoType == "MANUAL"))
    }

    private fun buyLotto() {
        runCatching {
            val seller =
                when (state.value.selectedSellerId) {
                    1 -> NormalLottoSeller()
                    2 -> DisCountLottoSeller()
                    3 -> NormalLottoVendingMachine()
                    4 -> NoisyLottoVendingMachine()
                    else -> error("로또 판매자를 선택해주세요.")
                }

            val amount = state.value.money.toIntOrNull() ?: error("구매할 금액을 입력해주세요.")

            when (val result = seller.lottoCount(amount)) {
                is LottoSellerResult.Failure.InsufficientFunds ->
                    error(
                        "금액이 부족합니다. 최소 ${result.requiredAmount}원 이상 입력하세요.",
                    )

                is LottoSellerResult.Failure.InvalidAmount ->
                    error(
                        "금액은 ${result.requiredUnit}원 단위로 입력해야 합니다.",
                    )

                is LottoSellerResult.Success<*> -> {}
            }

            val shape =
                if (state.value.showWidth) {
                    when (state.value.showHeight) {
                        true ->
                            Rectangle.Companion.create(
                                state.value.width.toInt(),
                                state.value.height.toInt(),
                            )

                        false -> Square.Companion.create(state.value.width.toInt())
                    }
                } else {
                    error("로또 모양을 선택해주세요.")
                }

            val selectedShape =
                when (shape) {
                    is ShapeResult.Success -> shape.value
                    is ShapeResult.Failure.InvalidShapeSize -> error("모양의 길이가 올바르지 않습니다.")
                }

            val type = state.value.lottoType ?: error("로또 구매 방식을 선택해주세요.")
            val lottoNumbers =
                state.value.manualNumbers.let {
                    requireNotNull(it) { "수동 로또 번호를 입력해주세요." }
                    val numbers = it.trim().split(",").map { it.trim().toInt() }
                    require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
                    numbers
                }

            val count = amount / seller.lottoPrice
            val lottoes =
                List(count) {
                    when (type) {
                        "AUTO" -> RandomLottoGenerateStrategy(selectedShape).lotto()
                        "MANUAL" ->
                            ManualLottoGenerateStrategy(lottoNumbers, selectedShape).lotto()

                        else -> error("잘못된 로또 구매 방식입니다.")
                    }
                }

            _state.value =
                LottoState(
                    result =
                        lottoes.joinToString("\n") {
                            "Numbers: ${it.numbers} " +
                                "(${
                                    when (selectedShape) {
                                        is Rectangle -> "가로: ${selectedShape.width}, 세로: ${selectedShape.height}"
                                        is Square -> "한 변의 길이: ${selectedShape.side}"
                                    }
                                })"
                        },
                    error = "",
                )
        }.onFailure {
            _state.value = LottoState(error = "⚠️ 오류: ${it.message}")
        }
    }
}
