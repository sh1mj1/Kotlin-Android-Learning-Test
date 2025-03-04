package com.example.learningtest.compose.state

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WellnessTasksListV1(
    list: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items = list,
            key = { task -> task.id },
        ) { task ->
            WellnessTaskItemV2(
                taskName = task.label,
                onClose = { onCloseTask(task) },
            )
        }
    }
}
