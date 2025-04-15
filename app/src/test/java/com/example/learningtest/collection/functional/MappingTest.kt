package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class MappingTest : FreeSpec({
    "mapping -  원소 변환" - {
        "map - 각 원소를 변환" {
            val numbers = listOf(1, 2, 3)
            numbers.map { number -> number * 2 } shouldBe listOf(2, 4, 6)
        }

        "mapIndexed - 인덱스를 함께 사용한 변환" {
            val names = listOf("a", "b", "c")
            names.mapIndexed { index, name -> "$index: $name" } shouldBe listOf(
                "0: a",
                "1: b",
                "2: c"
            )
        }

        "mapNotNull - null 을 걸러내며 변환" {
            val tags = listOf("1", "a", "2")
            val numberTags = tags.mapNotNull { tag -> tag.toIntOrNull() }

            numberTags shouldBe listOf(1, 2)
        }

        "mapIndexedNotNull - 인덱스를 함께 사용하며 null 을 걸러내며 변환" {
            val tags = listOf("1", "a", "2", null)
            val numberTags = tags
                .mapIndexedNotNull { index, tag ->
                    if (index == 0) return@mapIndexedNotNull null

                    tag?.toIntOrNull()
                }
            numberTags shouldBe listOf(2)
        }

        "flatMap - 각 요소를 여러 개로 펼친 후 평탄화" {
            val nested =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )

            val flattened = nested.flatMap { it }
            flattened shouldBe listOf(1, 2, 3, 4)
        }

        "associate - List 를 Map 으로 변환 (Key, Value 모두 수동 지정)" {
            val words = listOf("apple", "banana")
            val wordsWithLength = words.associate { word -> word to word.length }
            wordsWithLength shouldBe mapOf("apple" to 5, "banana" to 6)
        }

        "associateBy - Key 만 지정하고 Value 는 원본 사용" {
            data class User(val id: Int, val name: String)

            val users = listOf(User(1, "jimmy"), User(2, "tim"))
            val usersByCode = users.associateBy { user ->
                user.id * 32
            }

            usersByCode shouldBe mapOf(
                32 to User(1, "jimmy"),
                64 to User(2, "tim"),
            )
        }

        "associateWith - value 만 지정하고 key 는 원본 사용" {
            val fruits = listOf("apple", "banana")
            val fruitsWithLength = fruits.associateWith { it.length }

            fruitsWithLength shouldBe mapOf("apple" to 5, "banana" to 6)
        }

    }
})