package com.example.learningtest.solid.an.mvi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LottoScreen(viewModel: LottoViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "환영합니다! 로또 판매자를 선택해주세요.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        SellerSelection(state.selectedSellerId, onSellerSelected = {
            viewModel.handleIntent(LottoIntent.SelectSeller(it))
        })

        // 구매 금액 입력
        MoneyInput(state.money, onMoneyChanged = {
            viewModel.handleIntent(LottoIntent.EnterMoney(it))
        })

        // 로또 모양 선택
        ShapeSelection(state.showWidth, state.showHeight, onShapeSelected = {
            viewModel.handleIntent(LottoIntent.SelectShape(it))
        })

        // 가로 / 세로 입력
        ShapeInputs(
            state.width,
            state.height,
            state.showWidth,
            state.showHeight,
            onWidthChanged = { viewModel.handleIntent(LottoIntent.EnterWidth(it)) },
            onHeightChanged = { viewModel.handleIntent(LottoIntent.EnterHeight(it)) },
        )

        // 자동 / 수동 선택
        LottoTypeSelection(state.lottoType, onLottoTypeSelected = {
            viewModel.handleIntent(LottoIntent.SelectLottoType(it))
        })

        // 수동 입력 필드
        ManualNumbersInput(state.manualNumbers, state.showManualNumbers, onNumbersChanged = {
            viewModel.handleIntent(LottoIntent.EnterManualNumbers(it))
        })

        // 구매 버튼
        BuyButton(onBuyClicked = {
            viewModel.handleIntent(LottoIntent.BuyLotto)
        })

        // 결과 / 오류 메시지 출력
        if (state.result.isNotEmpty()) {
            Text(
                state.result,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (state.error.isNotEmpty()) {
            Text(state.error, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun SellerSelection(
    selectedSellerId: Int?,
    onSellerSelected: (Int) -> Unit,
) {
    Column {
        RadioButtonGroup(
            options =
                listOf(
                    "일반 로또 판매자 (1000원)" to 1,
                    "할인 로또 판매자 (500원)" to 2,
                    "일반 로또 자판기 (1000원)" to 3,
                    "시끄러운 로또 자판기 (1000원)" to 4,
                ),
            selectedOption = selectedSellerId,
            onSelected = onSellerSelected,
        )
    }
}

@Composable
fun MoneyInput(
    money: String,
    onMoneyChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = money,
        onValueChange = onMoneyChanged,
        label = { Text("구매할 금액을 입력하세요") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ShapeSelection(
    showWidth: Boolean,
    showHeight: Boolean,
    onShapeSelected: (Int) -> Unit,
) {
    Column {
        RadioButtonGroup(
            options = listOf("직사각형" to 1, "정사각형" to 2),
            selectedOption =
                if (showWidth) {
                    if (showHeight) {
                        1
                    } else {
                        2
                    }
                } else {
                    null
                },
            onSelected = onShapeSelected,
        )
    }
}

@Composable
fun ShapeInputs(
    width: String,
    height: String,
    showWidth: Boolean,
    showHeight: Boolean,
    onWidthChanged: (String) -> Unit,
    onHeightChanged: (String) -> Unit,
) {
    if (showWidth) {
        OutlinedTextField(
            value = width,
            onValueChange = onWidthChanged,
            label = { Text("가로 길이를 입력하세요") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    if (showHeight) {
        OutlinedTextField(
            value = height,
            onValueChange = onHeightChanged,
            label = { Text("세로 길이를 입력하세요") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun LottoTypeSelection(
    lottoType: String?,
    onLottoTypeSelected: (Int) -> Unit,
) {
    Column {
        RadioButtonGroup(
            options = listOf("자동 생성" to 1, "수동 입력" to 2),
            selectedOption =
                if (lottoType == "AUTO") {
                    1
                } else if (lottoType == "MANUAL") {
                    2
                } else {
                    null
                },
            onSelected = onLottoTypeSelected,
        )
    }
}

@Composable
fun ManualNumbersInput(
    manualNumbers: String,
    showManualNumbers: Boolean,
    onNumbersChanged: (String) -> Unit,
) {
    if (showManualNumbers) {
        OutlinedTextField(
            value = manualNumbers,
            onValueChange = onNumbersChanged,
            label = { Text("6개의 숫자를 쉼표로 구분하여 입력하세요") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun BuyButton(onBuyClicked: () -> Unit) {
    Button(
        onClick = onBuyClicked,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
    ) {
        Text("로또 구매", fontSize = 18.sp, color = Color.White)
    }
}

@Composable
fun RadioButtonGroup(
    options: List<Pair<String, Int>>,
    selectedOption: Int?,
    onSelected: (Int) -> Unit,
) {
    Column {
        options.forEach { (label, id) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(id) },
            ) {
                RadioButton(
                    selected = selectedOption == id,
                    onClick = { onSelected(id) },
                )
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LottoScreenPreview() {
    LottoScreen()
}
