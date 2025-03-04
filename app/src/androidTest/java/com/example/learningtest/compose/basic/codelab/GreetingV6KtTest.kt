package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class GreetingV6KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun show_more_button_is_shown_at_first() {
        // given && when
        composeTestRule.setContent {
            GreetingV6(name = "Android")
        }

        // then
        composeTestRule.onNodeWithText("Show more").assertIsDisplayed()
        composeTestRule.onNodeWithText("Show less").assertIsNotDisplayed()
    }

    @Test
    fun show_less_button_is_shown_after_click_the_button() {
        // given
        composeTestRule.setContent {
            GreetingV6(name = "Android")
        }

        // when
        composeTestRule.onNodeWithText("Show more").performClick()

        // then
        composeTestRule.onNodeWithText("Show less").assertIsDisplayed()
        composeTestRule.onNodeWithText("Show more").assertIsNotDisplayed()
    }
}
