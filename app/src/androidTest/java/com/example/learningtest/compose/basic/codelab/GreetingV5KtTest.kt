package com.example.learningtest.compose.basic.codelab

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GreetingV5KtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greetingV5_display_button_named_Show_more() {
        // given && when
        composeTestRule.setContent {
            GreetingV5(name = "Android")
        }

        val buttonMatcher =
            SemanticsMatcher.expectValue(
                SemanticsProperties.Role,
                Role.Button,
            )

        // then
        composeTestRule.onNode(matcher = buttonMatcher)
            .assertTextContains("Show more")
    }

    @Test
    fun greetingV5_root_has_one_node_Surface() {
        // given && when
        composeTestRule.setContent {
            GreetingV5(name = "Android")
        }

        composeTestRule.onRoot()
            .onChildren()
            .assertCountEquals(1)
    }

    @Test
    fun greetingV5_Surface_has_three_nodes() {
        // given && when
        composeTestRule.setContent {
            GreetingV5(name = "Android")
        }

        assertThrows<AssertionError> {
            composeTestRule.onRoot()
                .onChild() // Surface
                .onChildren() // Row
                .assertCountEquals(1)
        }

        composeTestRule.onRoot()
            .onChild() // Surface
            .onChildren() // Row
            .assertCountEquals(3) // Text("Hello"), Text("Android"), Button("Show more")
    }

    @Test
    fun greetingV5WithTestTag_Surface_has_one_node_Row() {
        // given && when
        composeTestRule.setContent {
            GreetingV5WithTestTag(name = "Android")
        }

        composeTestRule.onRoot()
            .onChild() // Surface
            .onChildren() // Row
            .assertCountEquals(1) // Row
    }

    @Test
    fun greetingV5WithTestTag_Row_has_two_nodes_Column_and_ElevatedButton() {
        // given && when
        composeTestRule.setContent {
            GreetingV5WithTestTag(name = "Android")
        }

        composeTestRule.onRoot()
            .onChild() // Surface
            .onChild() // Row
            .onChildren() // Column, ElevatedButton("Show more")
            .assertCountEquals(2)
    }

    @Test
    fun greetingV5WithTestTag_Column_has_two_nodes_Column_and_ElevatedButton() {
        // given && when
        composeTestRule.setContent {
            GreetingV5WithTestTag(name = "Android")
        }

        composeTestRule.onRoot()
            .onChild() // Surface
            .onChild() // Row
            .onChildren()[0] // Column
            .onChildren() // Text("Hello"), Text("Android")
            .assertCountEquals(2)
    }

    @Test
    fun greetingV5WithSemanticsTest() {
        composeTestRule.setContent {
            GreetingV5WithSemantic(name = "Android")
        }
        composeTestRule.onRoot()
            .onChild() // Surface
            .onChildren() // Row
            .assertCountEquals(1)

        composeTestRule.onRoot()
            .onChild() // Surface
            .onChild() // Row
            .onChildren() // Column, ElevatedButton("Show more")
            .assertCountEquals(2)

        composeTestRule.onRoot()
            .onChild() // Surface
            .onChild() // Row
            .onChildren()[0] // Column
            .onChildren() // Text("Hello"), Text("Android")
            .assertCountEquals(2)
    }
}
