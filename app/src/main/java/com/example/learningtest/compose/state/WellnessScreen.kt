package com.example.learningtest.compose.state

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        WaterStatefulCounter()
        WellnessTasksList()
    }
}

@Preview(showBackground = true)
@Composable
private fun WellnessScreenPreview() {
    WellnessScreen()
}
