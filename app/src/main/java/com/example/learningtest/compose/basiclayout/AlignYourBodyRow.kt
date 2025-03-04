package com.example.learningtest.compose.basiclayout

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningtest.R

@Composable
fun AlignYourBodyRow(modifier: Modifier = Modifier) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        items(alignYourBodyData()) { item ->
            AlignYourBodyElement(
                drawable = item.drawableRes,
                text = item.text,
            )
        }
    }
}

data class AlignYourBodyData(
    @StringRes val text: Int,
    @DrawableRes val drawableRes: Int,
)

fun alignYourBodyData(): List<AlignYourBodyData> =
    List(100) { i ->
        AlignYourBodyData(
            text = R.string.align_your_body_data_sample,
            drawableRes = R.drawable.ic_launcher_background,
        )
    }

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
private fun AlignYourBodyRowPreview() {
    AlignYourBodyRow()
}
