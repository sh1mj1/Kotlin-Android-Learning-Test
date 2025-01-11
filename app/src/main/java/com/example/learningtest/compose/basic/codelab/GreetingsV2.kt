package com.example.learningtest.compose.basic.codelab

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningtest.ui.theme.LearningTestTheme

@Composable
fun GreetingsV2(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" },
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            GreetingV7(name = name)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingsV2Preview() {
    LearningTestTheme {
        GreetingsV2()
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "GreetingPreviewDark",
)
@Composable
fun GreetingV2DarkModePreview() {
    LearningTestTheme {
        GreetingsV2()
    }
}
