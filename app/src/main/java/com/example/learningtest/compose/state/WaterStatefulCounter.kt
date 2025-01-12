package com.example.learningtest.compose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WaterStatefulCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableIntStateOf(0) }
    WaterStatelessCounter(
        count = count,
        onIncrement = { count++ },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun WaterStatefulCounterPreview() {
    WaterStatefulCounter()
}
