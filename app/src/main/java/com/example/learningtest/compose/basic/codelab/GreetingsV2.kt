package com.example.learningtest.compose.basic.codelab

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GreetingsV2(
    modifier: Modifier = Modifier,
    names: List<String>,
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            GreetingV6(name = name)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingsV2Preview() {
    GreetingsV2(names = List(1000) { "$it" })
}
