package com.example.learningtest.kotest

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * ExpectSpec is similar to FunSpec and ShouldSpec but uses the expect keyword.
 *
 * ExpectSpec 은 FunSpec 과 ShouldSpec 과 비슷하지만 expect 키워드를 사용합니다.
 */
class KotestExpectSpec : ExpectSpec(
    {
        context("a calculator") {
            expect("simple addition") {
                2 + 3 shouldBe 5
            }
            expect("integer overflow") {
                @Suppress("INTEGER_OVERFLOW")
                val result = Int.MAX_VALUE + 1
                result shouldBe Int.MIN_VALUE
            }
        }
    },
)
