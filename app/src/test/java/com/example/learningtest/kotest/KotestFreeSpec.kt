package com.example.learningtest.kotest

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

/**
 * FreeSpec allows you to nest arbitrary levels of depth using the keyword - (minus) for outer tests, and just the test name for the final test:
 *
 * 말 그대로 깊이의 중첩을 자유롭게 할 수 있습니다.
 *
 * FreeSpec 은 바깥쪽 테스트에 - `-` 키워드를 사용하고 마지막에 진짜 테스트에는 테스트 이름만 사용하여 테스트합니다.
 */
class KotestFreeSpec : FreeSpec({
    "String.length" - {
        "should return the length of the string" {
            "sammy".length shouldBe 5
            "".length shouldBe 0
        }
    }
    "containers can be nested as deep as you want" - {
        "and so we nest another container" - {
            "like this! we are in another container yet" - {
                "finally a real test" {
                    1 + 1 shouldBe 2
                }
            }
        }
    }
})
