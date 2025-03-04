package com.example.learningtest.compose.basic.codelab

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningtest.ui.theme.LearningTestTheme

/**
 * Greeting use Surface and Modifier.
 */
@Composable
fun GreetingV3(
    name: String,
    modifier: Modifier = Modifier,
) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingV3Preview() {
    LearningTestTheme {
        MyAppV1()
    }
}
