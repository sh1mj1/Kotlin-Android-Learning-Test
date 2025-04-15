package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class MappingTest : FreeSpec({
    "리스트 컬렉션의 mapping -  원소 변환" - {
        "map - 각 원소를 변환" {
            val numbers = listOf(1, 2, 3)
            numbers.map { number -> number * 2 } shouldBe listOf(2, 4, 6)
        }

        "mapIndexed - 인덱스를 함께 사용한 변환" {
            val names = listOf("a", "b", "c")
            names.mapIndexed { index, name -> "$index: $name" } shouldBe
                listOf(
                    "0: a",
                    "1: b",
                    "2: c",
                )
        }

        "mapNotNull - null 을 걸러내며 변환" {
            val tags = listOf("1", "a", "2")
            val numberTags = tags.mapNotNull { tag -> tag.toIntOrNull() }

            numberTags shouldBe listOf(1, 2)
        }

        "mapIndexedNotNull - 인덱스를 함께 사용하며 null 을 걸러내며 변환" {
            val tags = listOf("1", "a", "2", null)
            val numberTags =
                tags
                    .mapIndexedNotNull { index, tag ->
                        if (index == 0) return@mapIndexedNotNull null

                        tag?.toIntOrNull()
                    }
            numberTags shouldBe listOf(2)
        }

        "mapTo - 결과를 미리 만든 리스트에 추가" {
            val source = listOf(1, 2, 3)
            val destination = mutableListOf(0, 0)
            source.mapTo(destination) { it * it }

            destination shouldBe listOf(0, 0, 1, 4, 9)
        }

        "mapIndexedTo -인덱스를 이용하고 결과를 미리 만든 리스트에 추가" {
            val source = listOf(1, 2, 3)
            val destination = mutableListOf<Pair<Int, Int>>()
            source.mapIndexedTo(destination) { index, value ->
                index to value
            }

            destination shouldBe listOf(0 to 1, 1 to 2, 2 to 3)
        }

        "mapNotNullTo - 결과를 미리 만든 리스트에 null 빼고 추가" {
            val source = listOf(1, 2, null)
            val destination = mutableListOf(0, 0)
            source.mapNotNullTo(destination) { it?.times(it) }

            destination shouldBe listOf(0, 0, 1, 4)
        }

        "mapIndexedNotNullTo - 인덱스를 이용하고 null 을 제거하며 리스트에 추가 " {
            val source = listOf("0", "a", "2", "b")
            val result = mutableListOf<Pair<Int, Int>>()

            source.mapIndexedNotNullTo(result) { index, value ->
                value.toIntOrNull()?.let { index to it }
            }

            result shouldBe listOf(0 to 0, 2 to 2)
        }

        "flatten - " {
            val nested =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )

            nested.flatten() shouldBe listOf(1, 2, 3, 4)
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

        "flatMapIndexed - " {
            val nested =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )

            val flattened =
                nested.flatMapIndexed { index, list ->
                    println("index: $index , list: $list")
                    list.map { value -> index * value }
                }

            flattened shouldBe listOf(0, 0, 3, 4)
        }

        "flatMapTo" {
            val source =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )
            val destination = mutableListOf(0)
            source.flatMapTo(destination) { it }

            destination shouldBe listOf(0, 1, 2, 3, 4)
        }

        "flatMapIndexed - " {
            val nested =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )
            val destination = mutableListOf(0, 1, 2)
            nested.flatMapIndexedTo(destination) { index, list ->
                list.map { value -> value * 2 }
            }

            destination shouldBe listOf(0, 1, 2, 2, 4, 6, 8)
        }

        "associate - List 를 Map 으로 변환 (Key, Value 모두 수동 지정)" {
            val words = listOf("apple", "banana")
            val wordsWithLength = words.associate { word -> word to word.length }
            wordsWithLength shouldBe mapOf("apple" to 5, "banana" to 6)
        }

        "associateBy - Key 만 지정하고 Value 는 원본 사용" {
            data class User(val id: Int, val name: String)

            val users = listOf(User(1, "jimmy"), User(2, "tim"))
            val usersByCode =
                users.associateBy { user ->
                    user.id * 32
                }

            usersByCode shouldBe
                mapOf(
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

/*
"mapIndexedNotNullTo - 인덱스를 이용하고 null 제거하며 리스트에 추가" {
        val source = listOf("0", "a", "2", "b")
        val result = mutableListOf<Pair<Int, Int>>()

        source.mapIndexedNotNullTo(result) { index, value ->
            value.toIntOrNull()?.let { index to it }
        }

        result shouldBe listOf(0 to 0, 2 to 2)
    }

    "associateTo - key, value 모두 지정하고 기존 Map에 추가" {
        val source = listOf("apple", "banana")
        val result = mutableMapOf<String, Int>()

        source.associateTo(result) { it to it.length }

        result shouldBe mapOf("apple" to 5, "banana" to 6)
    }

    "associateByTo - key만 지정하고 value는 원본 유지하며 기존 Map에 추가" {
        data class User(val id: Int, val name: String)
        val users = listOf(User(1, "A"), User(2, "B"))
        val result = mutableMapOf<Int, User>()

        users.associateByTo(result) { it.id }

        result[1]?.name shouldBe "A"
    }

    "associateWithTo - key는 원본 그대로, value는 지정한 값으로 기존 Map에 추가" {
        val keys = listOf("apple", "banana")
        val result = mutableMapOf<String, Int>()

        keys.associateWithTo(result) { it.length }

        result shouldBe mapOf("apple" to 5, "banana" to 6)
    }


* */
