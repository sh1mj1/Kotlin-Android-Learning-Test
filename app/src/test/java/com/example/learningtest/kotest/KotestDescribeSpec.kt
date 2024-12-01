package com.example.learningtest.kotest

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

/**
 * DescribeSpec offers a style familiar to those from a Ruby or Javascript background, as this testing style uses `describe` / `it` keywords.
 *
 * Tests must be nested in one or more describe blocks.
 *
 * 루비 혹은 자바 스크립트를 배웠던 사람들에게 익숙한 스타일을 제공합니다. `describe` / `it` 키워드를 사용합니다.
 */
class KotestDescribeSpec : DescribeSpec({
    describe("the sum of 2 and 3") {
        it("should be 5") {
            2 + 3 shouldBe 5
        }
        it("should not be 6") {
            2 + 3 shouldNotBe 6
        }
    }

    describe("strings.length") {
        it("should return the size of the string") {
            "sammy".length shouldBe 5
            "".length shouldBe 0
        }
    }
})
