package com.example.learningtest.compose.basiclayout

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.learningtest.ui.theme.LearningTestTheme

@Composable
fun MySootheAppLandscape(modifier: Modifier = Modifier) {
    LearningTestTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row {
                SootheNavigationRail()
                HomeScreen()
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 640,
    heightDp = 360,
)
@Composable
private fun MySootheAppLandscapePreview() {
    MySootheAppLandscape()
}
