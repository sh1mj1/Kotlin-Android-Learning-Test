package com.example.learningtest.solid.an.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningtest.solid.pure.DisCountLottoSeller
import com.example.learningtest.solid.pure.LottoSellerResult
import com.example.learningtest.solid.pure.ManualLottoGenerateStrategy
import com.example.learningtest.solid.pure.NoisyLottoVendingMachine
import com.example.learningtest.solid.pure.NormalLottoSeller
import com.example.learningtest.solid.pure.NormalLottoVendingMachine
import com.example.learningtest.solid.pure.RandomLottoGenerateStrategy
import com.example.learningtest.solid.pure.Rectangle
import com.example.learningtest.solid.pure.ShapeResult
import com.example.learningtest.solid.pure.Square
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LottoViewModel : ViewModel() {
    private val _state = MutableStateFlow(LottoState())
    val state: StateFlow<LottoState> get() = _state

    fun handleIntent(intent: LottoIntent) {
        viewModelScope.launch {
            when (intent) {
                is LottoIntent.SelectSeller -> updateSeller(intent.sellerId)
                is LottoIntent.SelectShape -> updateShape(intent.shapeId)
                is LottoIntent.SelectLottoType -> updateLottoType(intent.typeId)
                is LottoIntent.EnterMoney -> _state.value = _state.value.copy(money = intent.amount)
                is LottoIntent.EnterWidth -> _state.value = _state.value.copy(width = intent.width)
                is LottoIntent.EnterHeight ->
                    _state.value =
                        _state.value.copy(height = intent.height)

                is LottoIntent.EnterManualNumbers ->
                    _state.value =
                        _state.value.copy(manualNumbers = intent.numbers)

                is LottoIntent.BuyLotto -> buyLotto()
            }
        }
    }

    private fun updateSeller(sellerId: Int) {
        _state.value = _state.value.copy(selectedSellerId = sellerId)
    }

    private fun updateShape(shapeId: Int) {
        val showWidth = shapeId == 1 || shapeId == 2
        val showHeight = shapeId == 1
        _state.value =
            _state.value.copy(showWidth = showWidth, showHeight = showHeight)
    }

    private fun updateLottoType(typeId: Int) {
        val lottoType =
            when (typeId) {
                1 -> "AUTO"
                2 -> "MANUAL"
                else -> null
            }
        _state.value =
            _state.value.copy(lottoType = lottoType, showManualNumbers = (lottoType == "MANUAL"))
    }

    private fun buyLotto() {
        runCatching {
            val seller =
                when (_state.value.selectedSellerId) {
                    1 -> NormalLottoSeller()
                    2 -> DisCountLottoSeller()
                    3 -> NormalLottoVendingMachine()
                    4 -> NoisyLottoVendingMachine()
                    else -> error("로또 판매자를 선택해주세요.")
                }

            val amount = _state.value.money.toIntOrNull() ?: error("구매할 금액을 입력해주세요.")

            when (val result = seller.lottoCount(amount)) {
                is LottoSellerResult.Failure.InsufficientFunds -> error("금액이 부족합니다. 최소 ${result.requiredAmount}원 이상 입력하세요.")
                is LottoSellerResult.Failure.InvalidAmount -> error("금액은 ${result.requiredUnit}원 단위로 입력해야 합니다.")
                is LottoSellerResult.Success<*> -> {}
            }

            val shape =
                if (_state.value.showWidth) {
                    when (_state.value.showHeight) {
                        true ->
                            Rectangle.create(
                                _state.value.width.toInt(),
                                _state.value.height.toInt(),
                            )

                        false -> Square.create(_state.value.width.toInt())
                    }
                } else {
                    error("로또 모양을 선택해주세요.")
                }

            val selectedShape =
                when (shape) {
                    is ShapeResult.Success -> shape.value
                    is ShapeResult.Failure.InvalidShapeSize -> error("모양의 길이가 올바르지 않습니다.")
                }

            val type = _state.value.lottoType ?: error("로또 구매 방식을 선택해주세요.")
            if (type == "MANUAL") {
                val numbers =
                    _state.value.manualNumbers.split(",").mapNotNull { it.trim().toIntOrNull() }
                require(numbers.size == 6) { "6개의 숫자를 입력해주세요." }
            }

            val count = amount / seller.lottoPrice
            val lottoNumbers =
                List(count) {
                    when (type) {
                        "AUTO" -> RandomLottoGenerateStrategy(selectedShape).lotto()
                        "MANUAL" ->
                            ManualLottoGenerateStrategy(
                                _state.value.manualNumbers.split(",").map { it.trim().toInt() },
                                selectedShape,
                            ).lotto()

                        else -> error("잘못된 로또 구매 방식입니다.")
                    }
                }

            _state.value =
                _state.value.copy(
                    result =
                        lottoNumbers.joinToString("\n") {
                            "Numbers: ${it.numbers} " +
                                "(${
                                    if (selectedShape is Rectangle) {
                                        "가로: ${selectedShape.width}, 세로: ${selectedShape.height}"
                                    } else {
                                        "한 변의 길이: ${(selectedShape as Square).side}"
                                    }
                                })"
                        },
                    error = "",
                )
        }.onFailure {
            _state.value = _state.value.copy(error = "⚠️ 오류: ${it.message}")
        }
    }
}
