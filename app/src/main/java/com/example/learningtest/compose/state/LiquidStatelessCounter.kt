package com.example.learningtest.compose.state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun LiquidStatelessCounter(
    count: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text(text = "You've had $count glasses. ")
        }
        Button(
            onClick = onIncrement,
            modifier = Modifier.padding(top = 8.dp),
            enabled = count < 10,
        ) {
            Text(text = "Add one")
        }
    }
}

class CountParameters : PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(0, 1, 5, 10, 11)
}

@Preview(showBackground = true)
@Composable
private fun LiquidStatelessCounterPreview(
    @PreviewParameter(CountParameters::class) count: Int,
) {
    LiquidStatelessCounter(
        count = count,
        onIncrement = {},
    )
}
