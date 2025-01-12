package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MyAppKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun show_onboarding_when_it_is_required() {
        composeTestRule.setContent {
            MyApp(onboardingRequired = true)
        }
        composeTestRule.onNodeWithText("This is based on Compose basic codelab")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Continue")
            .assertIsDisplayed()
    }

    @Test
    fun not_show_onboarding_when_it_is_not_required() {
        composeTestRule.setContent {
            MyApp(onboardingRequired = false)
        }
        composeTestRule.onNodeWithText("This is based on Compose basic codelab")
            .assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Continue")
            .assertIsNotDisplayed()
    }

    @Test
    fun show_greetings_when_continue_clicked() {
        composeTestRule.setContent {
            MyApp(onboardingRequired = true)
        }

        composeTestRule.onNodeWithText("Continue")
            .performClick()

        composeTestRule.onAllNodesWithText("Hello")
            .onFirst()
            .assertIsDisplayed()

        composeTestRule.onNode(hasScrollAction())
            .assertIsDisplayed()
    }
}
