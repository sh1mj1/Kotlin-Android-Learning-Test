package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import org.junit.Rule
import org.junit.Test

class GreetingV4KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greetingV4_displaysTwoTextElementsCorrectly() {
        // given && when
        composeTestRule.setContent {
            GreetingV4(name = "Android")
        }

        // then
        composeTestRule.onNodeWithText("Hello")
            .assertIsDisplayed()

        val columnChildren =
            composeTestRule.onNodeWithText("Hello")
                .onParent()
                .onChildren()

        columnChildren.assertCountEquals(2)
        columnChildren[0].assert(hasText("Hello"))
        columnChildren[1].assert(hasText("Android"))
    }
}
