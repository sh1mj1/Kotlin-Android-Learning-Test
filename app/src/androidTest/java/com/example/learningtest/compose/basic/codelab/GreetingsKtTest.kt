package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import org.junit.Rule
import org.junit.Test

class GreetingsKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun scroll_and_display_last_item() {
        composeTestRule.setContent {
            Greetings(names = List(1000) { "$it" })
        }

        composeTestRule.onNodeWithText("1")
            .assertIsDisplayed()

        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(999)

        composeTestRule.onNodeWithText("999")
            .assertIsDisplayed()
    }

    @Test
    fun saving_show_more_or_less_state() {
        // given
        composeTestRule.setContent {
            Greetings(names = List(1000) { "$it" })
        }
        val buttonMatcher =
            SemanticsMatcher.expectValue(
                SemanticsProperties.Role,
                Role.Button,
            )

        // when
        composeTestRule.onAllNodes(buttonMatcher).onFirst()
            .performClick()
        composeTestRule.onNodeWithText("ipsum", substring = true)
            .assertIsDisplayed()

        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(999)

        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(0)

        // then
        composeTestRule.onNodeWithText("ipsum", substring = true)
            .assertIsDisplayed()
    }
}
