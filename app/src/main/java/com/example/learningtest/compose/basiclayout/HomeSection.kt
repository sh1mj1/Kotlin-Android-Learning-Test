package com.example.learningtest.compose.basiclayout

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningtest.R
import com.example.learningtest.ui.theme.LearningTestTheme

@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
        )

        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeSectionPreview() {
    LearningTestTheme {
        HomeSection(R.string.align_your_body) {
            AlignYourBodyRow()
        }
    }
}
