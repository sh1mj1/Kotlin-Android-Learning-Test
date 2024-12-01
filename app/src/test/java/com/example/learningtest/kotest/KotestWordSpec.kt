package com.example.learningtest.kotest

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

/**
 * WordSpec uses the keyword should and uses that to nest tests after a context string.
 *
 * It also supports the keyword When allowing to add another level of nesting.
 *
 * WordSpec 은 should 키워드를 사용하여 context 문자열 뒤에 테스트를 중첩합니다.
 *
 * 또한 when 키워드를 지원하여 또 다른 중첩을 추가할 수 있습니다.
 */
class KotestWordSpec : WordSpec({
    "String.length" should {
        "return the length of the string" {
            "sammy".length shouldBe 5
            "".length shouldBe 0
        }
    }
    "Hello" When {
        "asked for length" should {
            "return 5" {
                "Hello".length shouldBe 5
            }
        }
        "appended to sh1mj1" should {
            "return Hello sh1mj1" {
                "Hello " + "sh1mj1" shouldBe "Hello sh1mj1"
            }
        }
    }
})
