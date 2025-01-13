package com.example.learningtest.compose.basiclayout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.learningtest.ui.theme.LearningTestTheme

@Composable
fun MySoothePortrait(modifier: Modifier = Modifier) {
    LearningTestTheme {
        Scaffold(
            bottomBar = { SootheBottomNavigation() },
        ) { paddingValues ->
            HomeScreen(Modifier.padding(paddingValues))
        }
    }
}

@Preview
@Composable
private fun MySoothePortraitPreview() {
    MySoothePortrait()
}
