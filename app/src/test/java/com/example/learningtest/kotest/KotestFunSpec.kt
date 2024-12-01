package com.example.learningtest.kotest

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * FunSpec allows you to create tests by invoking a function called test with a string argument to describe the test, and then the test itself as a lambda.
 *
 * FunSpec 은 테스트를 설명하는 문자열 인수를 사용하여 test 라는 함수를 호출한 다음 람다로 테스트 자체를 만들 수 있습니다.
 */
class KotestFunSpec : FunSpec({
    test("the sum of 2 and 3 is 5") {
        2 + 3 shouldBe 5
    }

    test("String length should return the length of the string") {
        "sammy".length shouldBe 5
        "".length shouldBe 0
    }
})
