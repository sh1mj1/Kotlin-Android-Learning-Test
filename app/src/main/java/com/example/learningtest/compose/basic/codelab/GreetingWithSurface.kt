package com.example.learningtest.compose.basic.codelab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GreetingWithSurface(
    name: String,
    modifier: Modifier = Modifier,
) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingWithSurfacePreview() {
    GreetingWithSurface(
        name = "Android",
        modifier = Modifier,
    )
}
