package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class GreetingKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_expanded_initially_then_ipsum_is_shown() {
        composeTestRule.setContent {
            Greeting(name = "Android", expandedInitially = true)
        }

        composeTestRule.onNode(hasText("ipsum", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun when_not_expanded_initially_then_ipsum_is_now_shown() {
        composeTestRule.setContent {
            Greeting(name = "Android", expandedInitially = false)
        }

        composeTestRule.onNode(hasText("ipsum", substring = true))
            .assertIsNotDisplayed()
    }

    @Test
    fun give_not_expanded_click_when_click_iconButton_then_ipsum_is_shown() {
        composeTestRule.setContent {
            Greeting(name = "Android", expandedInitially = false)
        }
        val buttonMatcher =
            SemanticsMatcher.expectValue(
                SemanticsProperties.Role,
                Role.Button,
            )
        composeTestRule.onNode(buttonMatcher).performClick()

        composeTestRule.onNode(hasText("ipsum", substring = true))
            .assertIsDisplayed()
    }
}
