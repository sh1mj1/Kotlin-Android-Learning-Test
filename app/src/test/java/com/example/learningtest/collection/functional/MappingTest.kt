package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class MappingTest : FreeSpec({
    "리스트 컬렉션의 mapping -  원소 변환" - {
        "map - 각 원소를 변환" {
            val numbers: List<Int> = listOf(1, 2, 3)
            numbers.map { number -> number * 2 } shouldBe listOf(2, 4, 6)
        }

        "mapIndexed - 인덱스를 함께 사용한 변환" {
            val names: List<String> = listOf("a", "b", "c")
            names.mapIndexed { index, name -> "$index: $name" } shouldBe
                listOf(
                    "0: a",
                    "1: b",
                    "2: c",
                )
        }

        "mapNotNull - null 을 걸러내며 변환" {
            val tags: List<String> = listOf("1", "a", "2")
            val numberTags: List<Int> = tags.mapNotNull { tag -> tag.toIntOrNull() }

            numberTags shouldBe listOf(1, 2)
        }

        "mapIndexedNotNull - 인덱스를 함께 사용하며 null 을 걸러내며 변환" {
            val tags: List<String?> = listOf("1", "a", "2", null)
            val numberTags: List<Int> =
                tags
                    .mapIndexedNotNull { index, tag ->
                        if (index == 0) return@mapIndexedNotNull null

                        tag?.toIntOrNull()
                    }
            numberTags shouldBe listOf(2)
        }

        "mapTo - 결과를 미리 만든 리스트에 추가" {
            val source: List<Int> = listOf(1, 2, 3)
            val destination: MutableList<Int> = mutableListOf(0, 0)
            source.mapTo(destination) { it * it }

            destination shouldBe listOf(0, 0, 1, 4, 9)
        }

        "mapIndexedTo -인덱스를 이용하고 결과를 미리 만든 리스트에 추가" {
            val source: List<Int> = listOf(1, 2, 3)
            val destination: MutableList<Pair<Int, Int>> = mutableListOf()
            source.mapIndexedTo(destination) { index, value ->
                index to value
            }

            destination shouldBe listOf(0 to 1, 1 to 2, 2 to 3)
        }

        "mapNotNullTo - 결과를 미리 만든 리스트에 null 빼고 추가" {
            val source: List<Int?> = listOf(1, 2, null)
            val destination: MutableList<Int> = mutableListOf(0, 0)
            source.mapNotNullTo(destination) { it?.times(it) }

            destination shouldBe listOf(0, 0, 1, 4)
        }

        "mapIndexedNotNullTo - 인덱스를 이용하고 null 을 제거하며 리스트에 추가 " {
            val source: List<String> = listOf("0", "a", "2", "b")
            val result: MutableList<Pair<Int, Int>> = mutableListOf()

            source.mapIndexedNotNullTo(result) { index, value ->
                value.toIntOrNull()?.let { index to it }
            }

            result shouldBe listOf(0 to 0, 2 to 2)
        }

        "associate - List 를 Map 으로 변환 (Key, Value 모두 수동 지정)" {
            val words: List<String> = listOf("apple", "banana")
            val wordsWithLength: Map<String, Int> = words.associate { word -> word to word.length }
            wordsWithLength shouldBe mapOf("apple" to 5, "banana" to 6)
        }

        "associateWith - value 만 지정하고 key 는 원본 사용" {
            val fruits: List<String> = listOf("apple", "banana")
            val fruitsWithLength: Map<String, Int> = fruits.associateWith { it.length }

            fruitsWithLength shouldBe mapOf("apple" to 5, "banana" to 6)
        }

        "associateBy - Key 만 지정하고 Value 는 원본 사용" {
            data class User(val id: Int, val name: String)

            val users: List<User> = listOf(User(1, "jimmy"), User(2, "tim"))
            val usersByCode: Map<Int, User> =
                users.associateBy { user ->
                    user.id * 32
                }

            usersByCode shouldBe
                mapOf(
                    32 to User(1, "jimmy"),
                    64 to User(2, "tim"),
                )
        }

        "associateTo - key 만 지정하고 value 는 원본 유지하며 기존 Map에 추가" {
            val fruits: List<String> = listOf("apple", "banana")
            val fruitsWithLength: MutableMap<String, Int> = mutableMapOf("lemon" to 5)

            fruits.associateTo(fruitsWithLength) { fruit ->
                fruit to fruit.length
            }

            fruitsWithLength shouldBe mapOf("lemon" to 5, "apple" to 5, "banana" to 6)
        }

        "associateWithTo - value 만 지정하고 key 는 원본 유지하며 기본 map 에 추가" {
            val fruits: List<String> = listOf("apple", "banana")
            val fruitsWithLength: MutableMap<String, Int> = mutableMapOf("lemon" to 5)

            fruits.associateWithTo(fruitsWithLength) { fruit ->
                fruit.length
            }

            fruitsWithLength shouldBe
                mapOf(
                    "lemon" to 5,
                    "apple" to 5,
                    "banana" to 6,
                )
        }

        "associateByTo - key만 지정하고 value 는 원본 유지하며 기존 map 에 추가" {
            data class User(val id: Int, val name: String)

            val users: List<User> = listOf(User(1, "A"), User(2, "B"))
            val codeWithUsers: MutableMap<Int, User> = mutableMapOf(10 to User(3, "ABC"))

            users.associateByTo(codeWithUsers) { user ->
                user.id + 32
            }
            codeWithUsers shouldBe
                mapOf(
                    10 to User(3, "ABC"),
                    33 to User(1, "A"),
                    34 to User(2, "B"),
                )
        }
    }
})
