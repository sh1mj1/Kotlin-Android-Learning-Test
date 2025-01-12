package com.example.learningtest.compose.state

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LiquidStatefulCounter(modifier: Modifier = Modifier) {
    var waterCount by rememberSaveable { mutableIntStateOf(0) }
    var juiceCount by rememberSaveable { mutableIntStateOf(0) }

    Column {
        LiquidStatelessCounter(
            count = waterCount,
            onIncrement = { waterCount++ },
        )

        LiquidStatelessCounter(
            count = juiceCount,
            onIncrement = { juiceCount++ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LiquidStatefulCounterPreview() {
    LiquidStatefulCounter()
}
