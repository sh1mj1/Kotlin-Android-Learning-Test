package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class FilteringTest : FreeSpec({
    "List Filter Api" - {
        "filter - 조건을 만족하는 것만 남기기" {
            val numbers = listOf<Int>(1, 2, 3, 4, 5, 6)
            numbers.filter { it % 2 == 0 } shouldBe listOf<Int>(2, 4, 6)
        }

        "filterNot - 조건을 만족하지 않는 요소만 남기기" {
            val words = listOf<String>("a", "be", "cat")
            words.filterNot { it.length > 1 } shouldBe listOf<String>("a")
        }

        "filterIndexed - 인덱스와 함께 필터링" {
            val words = listOf<String>("zero", "one", "two", "three")
            val result = words.filterIndexed { index, _ -> index % 2 == 0 }
            result shouldBe listOf<String>("zero", "two")
        }

        "take - 앞에서부터 N개 가져오기" {
            val numbers = listOf<Int>(0, 1, 2, 3, 4)
            numbers.take(2) shouldBe listOf<Int>(0, 1)
        }

        "drop - 앞에서부터 N 개 버리고 나머지 가져오기" {
            val numbers = listOf<Int>(0, 1, 2, 3, 4)
            numbers.drop(2) shouldBe listOf<Int>(2, 3, 4)
        }

        "takeWhile - 앞에서부터 조건이 거짓이기 전까지 가져오기(거짓이되면 순회 중지)" {
            val numbers = listOf<Int>(1, 2, 3, 0, 4)
            numbers.takeWhile { number -> number > 0 } shouldBe listOf<Int>(1, 2, 3)
        }

        "dropWhile - 앞에서부터 조건이 참이기 전까지 버리고 나머지 가져오기(참이 되면 순회 중지)" {
            val numbers = listOf<Int>(1, 2, 3, 0, 4)
            numbers.dropWhile { it > 0 } shouldBe listOf<Int>(0, 4)
        }

        "filterTo - 필터링 결과를 기존 컬렉션에 추가" {
            val target = mutableListOf<Int>(0, 9)
            val source = listOf<Int>(1, 2, 3, 4)
            source.filterTo(target) { it > 2 }

            target shouldBe listOf<Int>(0, 9, 3, 4)
        }

        "filterIndexedTo - 인덱스와 함께 필터링 결과를 기존 컬렉션에 추가" {
            val target = mutableListOf<String>("hello", "world")
            val source = listOf<String>("a", "b", "c", "d")
            source.filterIndexedTo(target) { index, _ -> index % 2 == 0 }

            target shouldBe listOf<String>("hello", "world", "a", "c")
        }

        "filterNotTo - 조건을 만족하지 않은 것만 필터링해서 기존 컬렉션에 추가" {
            val target = mutableListOf<Int>(3, 3)
            val source = listOf<Int>(1, 2, 3, 4)
            source.filterNotTo(target) { it % 2 == 0 }

            target shouldBe listOf<Int>(3, 3, 1, 3)
        }

        "filterIsInstance - 타입 필터링" {
            val list: List<Any> = listOf(1, "a", 2L, 'b', "c")
            list.filterIsInstance<String>() shouldBe listOf("a", "c")
        }

        "filterIsInstanceTo - 타입 필터링 후 다른 컬렉션에 저장" {
            val target = mutableListOf<String>("d")
            val source = listOf<Any>("a", 1, 'b', 2L, "c")

            source.filterIsInstanceTo(target) shouldBe listOf<String>("d", "a", "c")
        }

        "distinct - 중복 제거" {
            val list = listOf<Int>(1, 2, 3, 1, 2)
            list.distinct() shouldBe listOf<Int>(1, 2, 3)
        }

        "distinctBy - 조건에 따라 중복 제거 (대표 요소 하나만 남김)" {
            val list = listOf<String>("aaa", "bbb", "ccc", "aa", "bb", "cc", "a", "b", "c")
            list.distinctBy { it.length } shouldBe listOf<String>("aaa", "aa", "a")
        }

        "partition - 조건에 따라 두 그룹으로 나누기" {
            val list = listOf(1, 2, 3, 4, 5)
            val (even, odd) = list.partition { it % 2 == 0 }

            even shouldBe listOf(2, 4)
            odd shouldBe listOf(1, 3, 5)
        }
    }

    "Map Filter Api" - {
        val map =
            mapOf<String, Int>(
                "a" to 1,
                "b" to 2,
                "c" to 3,
                "d" to 4,
            )

        "filter - (key, value) 쌍으로 필터링" {
            // requires a pair of parentheses around the key and value in the lambda block.
            val result =
                map.filter { (key, value) -> key in listOf<String>("a", "c") && value % 2 == 1 }

            result shouldBe mapOf<String, Int>("a" to 1, "c" to 3)
        }

        "filterKeys - key 기준 필터링" {
            val result = map.filterKeys { it > "b" }
            result shouldBe mapOf<String, Int>("c" to 3, "d" to 4)
        }

        "filterValues - value 기준 필터링" {
            val result = map.filterValues { it % 2 == 0 }
            result shouldBe mapOf<String, Int>("b" to 2, "d" to 4)
        }

        "filterNot - 조건을 만족하지 않는 (key, value) 필터링" {
            val result = map.filterNot { (_, value) -> value > 2 }
            result shouldBe mapOf("a" to 1, "b" to 2)
        }

        "filterTo - 조건을 만족하는 항목을 다른 MutableMap에 추가" {
            val source = mapOf("one" to 1, "two" to 2, "three" to 3)
            val target = mutableMapOf<String, Int>()

            source.filterTo(target) { (_, v) -> v % 2 == 1 }

            target shouldBe mapOf("one" to 1, "three" to 3)
        }

        "filterNotTo - 조건을 만족하지 않는 항목을 target에 추가" {
            val source = mapOf("a" to 1, "b" to 2, "c" to 3)
            val result = mutableMapOf<String, Int>()

            source.filterNotTo(result) { (_, v) -> v > 1 }

            result shouldBe mapOf("a" to 1)
        }
    }
})
