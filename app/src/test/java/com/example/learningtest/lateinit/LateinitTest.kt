package com.example.learningtest.lateinit

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LateinitTest : FreeSpec({
    "throw UninitializedPropertyAccessException when you access the lateinit property before initialization" {
        val user = User("sh1mj1")
        shouldThrow<UninitializedPropertyAccessException> {
            user.introduction
        }
    }

    "lateinit property should be initialized before use" {
        val user = User("sh1mj1")
        user.initIntroduction()
        user.introduction shouldBe "Hello, my name is sh1mj1"
    }
})

private data class User(val name: String) {
    lateinit var introduction: String

    fun initIntroduction() {
        introduction = "Hello, my name is $name"
    }
}
