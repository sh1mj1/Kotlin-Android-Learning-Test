package com.example.learningtest.compose.state

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun WellnessTaskItemV3(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            text = taskName,
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WellnessTaskItemPreview(
    @PreviewParameter(WellnessTaskParameters::class) checked: Boolean,
) {
    WellnessTaskItemV3(
        taskName = "This is a taskName",
        checked = checked,
        onCheckedChange = {},
        onClose = {},
    )
}

class WellnessTaskParameters : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> =
        sequenceOf(
            true,
            false,
        )
}
