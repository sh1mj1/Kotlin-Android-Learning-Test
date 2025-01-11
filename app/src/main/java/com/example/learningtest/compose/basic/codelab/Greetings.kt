package com.example.learningtest.compose.basic.codelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String>,
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            GreetingV6(name = name)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingV7Preview() {
    Greetings(names = listOf("World", "Android"))
}
