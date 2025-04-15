package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ListFunctionalApiTest : FreeSpec({
    "map -  원소 변환" - {
        val names = listOf("a", "b", "c")
        val users = names.map { "$it!" }

        users shouldBe listOf("a!", "b!", "c!")
    }

    "filter - 조건에 맞는 원소만 남기기" - {
        val numbers = listOf(1, 2, 3, 4, 5)
        val evens = numbers.filter { it % 2 == 0 }

        evens shouldBe listOf(2, 4)
    }

    "find == firstOrNull - 조건을 만족하는 첫 요소 찾기 " - {
        val names = listOf("kim", "lee", "park")
        val found = names.find { it.startsWith("p") }
        val firstOrNull = names.firstOrNull { it.startsWith("p") }

        found shouldBe "park"
        firstOrNull shouldBe "park"
    }

    "any, all, none - 조건 검증" - {
        val numbers = listOf(1, 2, 3, 4)

        numbers.any { it > 3 } shouldBe true
        numbers.all { it < 10 } shouldBe true
        numbers.none { it < 0 } shouldBe true
    }

    "flatMap - 리스트 안의 리스트 펼치기" - {
        val nested =
            listOf(
                listOf(1, 2),
                listOf(3, 4),
            )

        val flattened = nested.flatMap { it }
        flattened shouldBe listOf(1, 2, 3, 4)
    }

    "groupBy - 조건에 따라 그룹화" - {
        val words = listOf("apple", "banana", "avocado", "blueberry")
        val grouped = words.groupBy { it.first() }

        grouped['a'] shouldBe listOf("apple", "avocado")
        grouped['b'] shouldBe listOf("banana", "blueberry")
    }

    "fold - 누적 계산(초기값 있음)" - {
        val numbers = listOf(1, 2, 3)
        val sum = numbers.fold(10) { acc, value -> acc + value }

        sum shouldBe 16
    }

    "reduce - 누적 계산(초기값 없음)" - {
        val numbers = listOf(1, 2, 3)
        val product = numbers.reduce { acc, value -> acc * value }

        product shouldBe 6
    }

    "zip - 두 리스트를 하나로 결합" - {
        val names = listOf("철수", "영희")
        val ages = listOf(20, 21)

        val result = names.zip(ages) { name, age -> "$name ($age)" }
        result shouldBe listOf("철수 (20)", "영희 (21)")
    }
})