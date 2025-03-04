package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class GreetingsV1KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test() {
        composeTestRule.setContent {
            GreetingsV1(names = List(1000) { "$it" })
        }

        composeTestRule.onNodeWithText("1")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("999")
            .assertIsNotDisplayed()

        composeTestRule.onNode(hasScrollAction()).assertDoesNotExist()
    }
}
