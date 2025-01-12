package com.example.learningtest.compose.basic.codelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.learningtest.ui.theme.LearningTestTheme

@Composable
fun MyAppV1(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        GreetingV3(
            name = "Android",
        )
    }
}

@Composable
fun MyAppV2(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose"),
) {
    Column(modifier) {
        for (name in names) {
            GreetingV4(name = name)
        }
    }
}

@Composable
fun MyAppV3(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose"),
) {
    Column(modifier) {
        for (name in names) {
            GreetingV5(name = name)
        }
    }
}

@Composable
fun MyAppV4(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreenV2(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            GreetingsV1(names = listOf("World", "Android"))
        }
    }
}

@Composable
fun MyAppV5(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreenV2(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            GreetingsV2(names = List(1000) { "$it" })
        }
    }
}

@Composable
fun MyAppV6(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreenV2(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            GreetingsV2(names = List(1000) { "$it" })
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    onboardingRequired: Boolean = true,
) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(onboardingRequired) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Preview
@Composable
private fun MyAppPreview() {
    LearningTestTheme {
        MyApp(modifier = Modifier.fillMaxSize(), onboardingRequired = true)
    }
}
