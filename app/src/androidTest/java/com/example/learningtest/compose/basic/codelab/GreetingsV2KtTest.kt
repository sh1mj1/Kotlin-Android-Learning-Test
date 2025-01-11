package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import org.junit.Rule
import org.junit.Test

class GreetingsV2KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun scroll_and_display_last_item() {
        composeTestRule.setContent {
            GreetingsV2(names = List(1000) { "$it" })
        }

        composeTestRule.onNodeWithText("1")
            .assertIsDisplayed()

        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(999)

        composeTestRule.onNodeWithText("999")
            .assertIsDisplayed()
    }
}
