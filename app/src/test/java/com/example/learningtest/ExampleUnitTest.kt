package com.example.learningtest

import io.kotest.matchers.shouldBe
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        2 + 2 shouldBe 4
    }

    fun dummyFoo(a: Int, b: Int): Int {
        return a + b;
    }

    @Test
    fun `실패하는 테스트`() {
        2 + 2 shouldBe 5
    }
}
