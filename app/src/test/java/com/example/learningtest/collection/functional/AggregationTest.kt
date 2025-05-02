package com.example.learningtest.collection.functional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class AggregationTest : FreeSpec({
    data class User(
        val name: String,
        val age: Int,
    )

    "List Aggregation" - {

        "count - 컬렉션의 요소 개수" {
            val numbers: List<Int> = listOf(1, 2, 3, 4, 5)

            numbers.count() shouldBe 5
            numbers.count { it % 2 == 0 } shouldBe 2
        }

        "sum - 컬렉션의 숫자 요소 합계" {
            val numbers: List<Int> = listOf(1, 2, 3, 4, 5)

            numbers.sum() shouldBe 15
        }

        "sumOf - " {
            val users: List<User> =
                listOf(
                    User("Alice", 30),
                    User("Bob", 25),
                    User("Charlie", 35),
                )

            users.sumOf { user ->
                user.age.takeIf { age -> age >= 30 } ?: 0
            } shouldBe 65
        }

        "average - 컬렉션의 숫자 요소 평균" {
            val numbers: List<Double> = listOf(1.0, 2.0, 3.0, 4.0, 5.0)

            numbers.average() shouldBe 3.0
            emptyList<Double>().average() shouldBe Double.NaN
        }

        "max - 컬렉션에서 가장 큰 요소(빈 컬렉션이면 NoSuchElementException 예외 발생)" {
            val numbers: List<Int> = listOf(10, 5, 20, 15)

            numbers.max() shouldBe 20
            shouldThrow<NoSuchElementException> {
                emptyList<Int>().max()
            }
        }

        "maxOrNull - 컬렉션에서 가장 큰 요소 (없으면 null)" {
            val numbers: List<Int> = listOf(10, 5, 20, 15)

            numbers.maxOrNull() shouldBe 20
            emptyList<Int>().maxOrNull() shouldBe null
        }

        "maxOf - 특정 속성 기준으로 컬렉션에서 가장 큰 요소 (빈 컬렉션이면 예외)" {
            val numbers: List<User> =
                listOf(
                    User("Alice", 30),
                    User("Bob", 25),
                    User("Charlie", 35),
                )

            numbers.maxOf { user -> user.age } shouldBe 35
        }

        "maxOfOrNull - 특정 속성 기준으로 컬렉션에서 가장 큰 요소 (없으면 null)" {
            val users: List<User> =
                listOf(
                    User("Alice", 30),
                    User("Bob", 25),
                    User("Charlie", 35),
                )

            users.maxOfOrNull(User::age) shouldBe 35
            emptyList<User>().maxOfOrNull(User::age) shouldBe null
        }

        "maxOfOrNull - 특정 속성 기준으로 컬렉션에서 가장 작은 요소 (없으면 null)" {
            val users: List<User> =
                listOf(
                    User("Alice", 30),
                    User("Bob", 25),
                    User("Charlie", 35),
                )

            users.minOfOrNull(User::age) shouldBe 25
            emptyList<User>().minOfOrNull(User::age) shouldBe null
        }

        "reduce - 첫 번째 요소부터 시작하여 누적 연산 (빈 컬렉션에는 사용 불가)" {
            val numbers: List<Int> = listOf(1, 2, 3, 4)

            // acc: 0, number: 1 -> 1
            // acc: 1, number: 2 -> 3
            // acc: 3, number: 3 -> 2
            // acc: 2, number: 4 -> 6
            numbers.reduce { acc, number ->
                if (acc >= number) acc - 1 else acc + number
            } shouldBe 6

            val words: List<String> = listOf("hello", " ", "world")

            words.reduce { acc, word -> acc + word } shouldBe "hello world"

            shouldThrow<UnsupportedOperationException> {
                emptyList<Int>().reduce { acc, i -> acc + i }
            }
        }

        "fold - 초기값을 가지고 누적 연산(빈 리스트도 가능)" {
            val numbers: List<Int> = listOf(1, 2, 3, 4)
            val initialValue = 10

            // acc: 10, number: 1 -> 11
            // acc: 11, number: 2 -> 11
            // acc: 11, number: 3 -> 14
            // acc: 14, number: 4 -> 18
            numbers.fold(initialValue) { acc, number ->
                if (number % 2 == 0) acc else acc + number
            }

            val words: List<String> = listOf("hello", " ", "world")
            val initialString = "Start: "
            words.fold(initialString) { acc, s -> acc + s } shouldBe "Start: hello world"

            emptyList<Int>().fold(0) { accumulator, element -> accumulator + element } shouldBe 0
        }

        "runningReduce - 각 단계의 누적 결과를 포함하는 리스트 반환 (빈 컬렉션에는 사용 불가)" { // New test case
            val numbers: List<Int> = listOf(1, 2, 3, 4)

            val runningSum: List<Int> = numbers.runningReduce { acc, number -> acc + number }
            runningSum shouldBe listOf<Int>(1, 3, 6, 10) // 1, 1 + 2, 1 + 2 + 3, 1 + 2 + 3 + 4
        }

        "runningFold - 초기값과 함께 각 단계의 누적 결과를 포함하는 리스트 반환 (빈 리스트도 가능)" { // New test case
            val numbers: List<Int> = listOf(1, 2, 3, 4)

            val runningNumbersFolded = numbers.runningFold(10) { acc, number -> acc + number }
            runningNumbersFolded shouldBe listOf(10, 11, 13, 16, 20)

            val words: List<String> = listOf("a", "b", "c")

            val runningWordsFolded = words.runningFold("Start: ") { acc, word -> acc + word }
            runningWordsFolded shouldBe listOf("Start: a", "Start: ab", "Start: abc")
        }
    }

    "Map Aggregation" - {
        val map: Map<String, Int> = mapOf("a" to 1, "b" to 2, "c" to 3, "d" to 4)

        "count - Map의 항목 개수" {
            map.count() shouldBe 4
            emptyMap<String, Int>().count() shouldBe 0
        }

        "reduce - Map.Entry의 누적 연산 (빈 Map에는 사용 불가)" {
            val map: Map<String, Int> = mapOf("a" to 1, "b" to 2, "c" to 3)

            val reducedEntry: Map.Entry<String, Int> =
                map.entries.reduce { acc, entry ->
                    object : Map.Entry<String, Int> {
                        override val key: String = acc.key + entry.key
                        override val value: Int = acc.value + entry.value
                    }
                }

            reducedEntry.key shouldBe "abc"
            reducedEntry.value shouldBe 6
        }

        "fold - Map.Entry의 누적 연산 (초기값 사용)" {
            val map: Map<String, Int> = mapOf("a" to 1, "b" to 2, "c" to 3)
            val initial: Map.Entry<String, Int> =
                object : Map.Entry<String, Int> {
                    override val key: String = "start "
                    override val value: Int = 100
                }

            val foldedValue: Map.Entry<String, Int> =
                map.entries.fold(initial) { acc, entry ->
                    object : Map.Entry<String, Int> {
                        override val key: String = acc.key + entry.key
                        override val value: Int = acc.value + entry.value
                    }
                }

            foldedValue.key shouldBe "start abc"
            foldedValue.value shouldBe 106
        }
    }
})
