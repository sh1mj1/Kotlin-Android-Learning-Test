package com.example.learningtest.compose.basic.codelab

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.learningtest.ui.theme.LearningTestTheme

/**
 * The basic composable function Text
 */
@Composable
fun GreetingV1(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingV1Preview() {
    LearningTestTheme {
        GreetingV1("Android")
    }
}
