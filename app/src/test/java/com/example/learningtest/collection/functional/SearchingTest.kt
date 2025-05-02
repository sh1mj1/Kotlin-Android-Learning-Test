package com.example.learningtest.collection.functional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class SearchingTest : FreeSpec({
    data class User(val name: String?, val age: Int)

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

    "indexOf - 해당 값의 첫 위치 (없으면 -1)" {
        val names: List<String> = listOf("jim", "pam", "jim")

        names.indexOf("jim") shouldBe 0
        names.indexOf("shim") shouldBe -1
    }

    "lastIndexOf - 해당 값의 마지막 위치 (없으면 -1)" {
        val names: List<String> = listOf("jim", "pam", "jim")

        names.lastIndexOf("jim") shouldBe 2
        names.lastIndexOf("shim") shouldBe -1
    }

    "indexOfFirst - 조건을 만족하는 원소 중 첫 인덱스 (없으면 -1)" {
        val names: List<String> = listOf("kim", "lee", "choi", "kim")

        names.indexOfFirst { it.startsWith("k") } shouldBe 0
        names.indexOfFirst { it == "park" } shouldBe -1
    }

    "indexOfLast - 조건을 만족하는 원소 중 마지막 인덱스(없으면 -1)" {
        val names: List<String> = listOf("kim", "lee", "choi", "kim")

        names.indexOfLast { it.startsWith("k") } shouldBe 3
        names.indexOfLast { it == "park" } shouldBe -1
    }

    "조건을 만족하는 원소가 없으면 -1 인덱스를 리턴하는 게 아닌 null 리턴하도록" {
        val names: List<String> = listOf("kim", "lee", "choi", "kim")

        names
            .indexOf("park")
            .takeIf { it != -1 } shouldBe null
        names
            .indexOfFirst { it.startsWith("p") }
            .takeIf { it != -1 } shouldBe null

        names
            .indexOfLast { it.startsWith("p") }
            .takeIf { it != -1 } shouldBe null
    }

    "firstNotNullOf - 변환 결과가 null 이 아닌 첫 값(없으면 예외)" {

        val users1: List<User> =
            listOf(
                User(null, 20),
                User("a", 25),
                User("b", 30),
            )
        val firstName = users1.firstNotNullOf { it.name }
        firstName shouldBe "a"

        val users2: List<User> = listOf(User(null, 20), User(null, 25))

        shouldThrow<NoSuchElementException> {
            users2.firstNotNullOf { it.name }
        }
    }

    "firstNotNullOfOrNull - 변환 결과가 null 이 아닌 첫 값(없으면 null)" {
        val users1: List<User> =
            listOf(
                User(null, 20),
                User(null, 25),
                User("Lilly", 30),
            )

        val firstName: String? = users1.firstNotNullOfOrNull { it.name }
        firstName shouldBe "Lilly"

        val users2: List<User> =
            listOf(
                User(null, 20),
                User(null, 25),
            )

        users2.firstNotNullOfOrNull { it.name } shouldBe null
    }

    "lastNotNullOf & lastNotNullOfOrNull 메서드는 없음 - 직접 구현 예시(mapNotNull 활용)" {
        val users: List<User> =
            listOf(
                User(null, 30),
                User(null, 25),
            )

        val lastName: String? = users.mapNotNull { it.name }.lastOrNull()
        lastName shouldBe null
    }
})
