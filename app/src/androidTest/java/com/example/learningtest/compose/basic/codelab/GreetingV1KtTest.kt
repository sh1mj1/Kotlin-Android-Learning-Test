package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class GreetingV1KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greeting_displaysCorrectName() {
        // given
        val name = "sh1mj1"

        // when
        composeTestRule.setContent {
            GreetingV1(name = name)
        }

        // then
        composeTestRule
            .onNodeWithText("Hello sh1mj1!")
            .assertExists()
    }
}
