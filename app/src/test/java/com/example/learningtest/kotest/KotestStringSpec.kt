package com.example.learningtest.kotest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * StringSpec reduces the syntax to the absolute minimum.
 *
 * Just write a string followed by a lambda expression with your test code.
 *
 * StringSpec 은 문법을 테스트 문법을 최소화합니다.
 * 테스트 코드에 대한 람다 표현식을 사용하여 문자열 뒤에 작성하면 됩니다.
 *
 */
class KotestStringSpec : StringSpec(
    {
        "the sum of 2 and 3 is 5" {
            2 + 3 shouldBe 5
        }
        "strings.length should return size of string".config(enabled = false, invocations = 3) {
            shouldThrow<Exception> {
                // This test is ignored
            }
        }
    },
)
