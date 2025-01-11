package com.example.learningtest.compose.basic.codelab

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import java.lang.AssertionError

@RunWith(AndroidJUnit4::class)
class BasicComposeActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule(BasicComposeActivity::class.java)

    @Test
    fun not_save_the_state_after_configuration_change() {
        // given
        composeTestRule.onNodeWithText("Continue")
            .assertIsDisplayed()

        // when
        composeTestRule.onNodeWithText("Continue")
            .performClick()
        composeTestRule.onNodeWithText("Continue")
            .assertDoesNotExist()

        composeTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // then
        assertThrows<AssertionError> {
            composeTestRule.onNodeWithText("Continue")
                .assertDoesNotExist()
        }
        composeTestRule.onNodeWithText("Continue")
            .assertExists()
    }
}
