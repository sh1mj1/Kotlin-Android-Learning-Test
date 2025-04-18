package com.example.learningtest.collection.functional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class SearchingTest : FreeSpec({
    "List Searching" - {
        "first - 조건을 만족하는 첫 요소 (없으면 예외 던짐)" {
            val numbers: List<Int> = listOf(1, 2, 3, 4)

            numbers.first() shouldBe 1
            numbers.first { it > 2 } shouldBe 3
            shouldThrow<NoSuchElementException> { numbers.first { it > 10 } }
        }

        "find == firstOrNull - 조건을 만족하는 첫 요소(없으면 null)" {
            val words: List<String> = listOf("apple", "banana", "cherry")

            words.find { it.startsWith("b") } shouldBe "banana"
            words.find { it.startsWith("z") } shouldBe null
            words.firstOrNull { it.startsWith("z") } shouldBe null
        }

        "last - 조건을 만족하는 마지막 요소 (없으면 예외)" {
            val numbers: List<Int> = listOf(1, 3, 5, 2, 4)

            numbers.last() shouldBe 4
            numbers.last { it % 2 == 1 } shouldBe 5
            shouldThrow<NoSuchElementException> { numbers.last { it > 10 } }
        }

        "findLast & lastOrNull - 조건을 만족하는 마지막 요소 (없으면 null)" {
            val numbers: List<Int> = listOf(1, 2, 3, 4)

            numbers.findLast { it % 2 == 1 } shouldBe 3
            numbers.findLast { it > 5 } shouldBe null

            numbers.lastOrNull() shouldBe 4
            numbers.lastOrNull { it > 5 } shouldBe null
        }

        "indexOf - 해당 값의 첫 위치 (없으면 -1)" {
            val names: List<String> = listOf("jim", "pam", "jim")

            names.indexOf("jim") shouldBe 0
            names.indexOf("shim") shouldBe -1
        }

        "all - 모든 요소가 조건을 만족하면 true" {
            val numbers: List<Int> = listOf(2, 4, 6)

            numbers.all { it % 2 == 0 } shouldBe true
            numbers.all { it > 4 } shouldBe false
        }

        "any - 조건을 만족하는 요소가 하나라도 있으면 true" {
            val numbers: List<Int> = listOf(1, 2, 3)

            numbers.any { it > 2 } shouldBe true
            numbers.any { it < 0 } shouldBe false
        }

        "none - 모든 요소가 조건을 만족하지 않으면 true" {
            val characters = listOf('a', 'b', 'c')

            characters.none { it == 'z' } shouldBe true
            characters.none { it == 'a' } shouldBe false
        }
    }
})

/*
@file:Suppress("TestFunctionName")

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

@DisplayName("고급 Searching API")
class AdvancedSearchingTest : FreeSpec({

    "indexOfFirst - 조건을 만족하는 첫 인덱스 (없으면 -1)" {
        val names = listOf("kim", "lee", "choi", "kim")
        names.indexOfFirst { it.startsWith("k") } shouldBe 0
        names.indexOfFirst { it == "park" } shouldBe -1
    }

    "indexOfLast - 조건을 만족하는 마지막 인덱스 (없으면 -1)" {
        val names = listOf("kim", "lee", "choi", "kim")
        names.indexOfLast { it.startsWith("k") } shouldBe 3
        names.indexOfLast { it == "park" } shouldBe -1
    }

    "firstNotNullOf - 변환 결과가 null 아닌 첫 값 (없으면 예외)" {
        data class User(val name: String?, val age: Int)
        val users = listOf(
            User(null, 20),
            User("a", 25),
            User("b", 30),
        )

        val name = users.firstNotNullOf { it.name }
        name shouldBe "a"

        shouldThrow<NoSuchElementException> {
            listOf<User>().firstNotNullOf { it.name }
        }
    }

    "firstNotNullOfOrNull - 변환 결과가 null 아닌 첫 값 (없으면 null)" {
        data class Item(val value: String?)
        val items = listOf(
            Item(null),
            Item(null),
            Item("found"),
        )

        val result = items.firstNotNullOfOrNull { it.value }
        result shouldBe "found"

        val result2 = items.take(2).firstNotNullOfOrNull { it.value }
        result2 shouldBe null
    }

    // 보너스: lastNotNullOf 없음 → 대체 방법
    "lastNotNullOfOrNull - 직접 구현 예시 (mapNotNull + lastOrNull)" {
        data class Log(val content: String?)
        val logs = listOf(
            Log(null),
            Log("warn"),
            Log("info"),
            Log(null),
        )

        val last = logs.mapNotNull { it.content }.lastOrNull()
        last shouldBe "info"
    }

})

* */
