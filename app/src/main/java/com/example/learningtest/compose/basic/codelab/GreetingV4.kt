package com.example.learningtest.compose.basic.codelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GreetingV4(
    name: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Column(modifier = modifier.fillMaxWidth().padding(24.dp)) {
            Text(text = "Hello ")
            Text(text = name)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingV4Preview() {
    GreetingV4(name = "Android")
}
