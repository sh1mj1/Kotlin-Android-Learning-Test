package com.example.learningtest.compose.basic.codelab

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningtest.ui.theme.LearningTestTheme

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" },
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    name = "GreetingDarkMode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
fun GreetingsPreview() {
    LearningTestTheme {
        Greetings()
    }
}
