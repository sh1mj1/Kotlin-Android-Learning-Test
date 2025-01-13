package com.example.learningtest.compose.state

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WellnessScreenV1(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        WaterStatefulCounter()

        val wellnessTasks = remember { wellnessTasks().toMutableStateList() }
        WellnessTasksList(list = wellnessTasks, onCloseTask = { task -> wellnessTasks.remove(task) })
    }
}

@Preview(showBackground = true)
@Composable
private fun WellnessScreenPreview() {
    WellnessScreenV1()
}
