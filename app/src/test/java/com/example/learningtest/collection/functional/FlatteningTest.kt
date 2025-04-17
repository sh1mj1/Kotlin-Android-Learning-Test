package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class FlatteningTest : FreeSpec({
    "List 의 flattening" - {
        "flatten - 중첩된 리스트를 평탄화" {
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

        "flatMapIndexed - 인덱스와 함께 각 요소를 여러 개로 펼친 후 평탄화" {
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

        "flatMapTo - 중첩된 리스트를 평탄화해서 기존 컬렉션에 추가" {
            val source =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )
            val destination = mutableListOf(0)
            source.flatMapTo(destination) { it }

            destination shouldBe listOf(0, 1, 2, 3, 4)
        }

        "flatMapIndexed - 인덱스와 함께 각 요소를 여러 개로 펼친 후 평탄화해서 기존 컬렉션에 추가" {
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
    }
})
