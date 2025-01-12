package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class OnboardingScreenV2KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private var shouldShowOnboarding = true

    @Test
    fun should_not_show_onboarding_when_click_continue_button() {
        // given
        composeTestRule.setContent {
            OnboardingScreenV2 {
                shouldShowOnboarding = false
            }
        }

        // when
        composeTestRule.onNodeWithText("Continue")
            .performClick()

        // then
        shouldShowOnboarding = false
    }
}
