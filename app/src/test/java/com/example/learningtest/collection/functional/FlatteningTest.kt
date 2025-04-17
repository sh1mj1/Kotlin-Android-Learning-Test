package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlin.collections.flatten

class FlatteningTest : FreeSpec({
    "List 의 flattening" - {
        "flatten - 중첩된 리스트를 평탄화" {
            val nested: List<List<Int>> =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )

            nested.flatten() shouldBe listOf(1, 2, 3, 4)
        }

        "flatten - 깊이 n 번의 중첩은 n 번의 flatten 으로 평탄화" {
            val nested: List<List<List<Int>>> =
                listOf(
                    listOf(
                        listOf(1, 2),
                        listOf(11, 22),
                    ),
                    listOf(
                        listOf(3, 4),
                        listOf(33, 44),
                    ),
                )

            val onceFlattened: List<List<Int>> = nested.flatten()
            val twiceFlattened: List<Int> = onceFlattened.flatten()

            onceFlattened shouldBe
                listOf(
                    listOf(1, 2),
                    listOf(11, 22),
                    listOf(3, 4),
                    listOf(33, 44),
                )
            twiceFlattened shouldBe listOf(1, 2, 11, 22, 3, 4, 33, 44)
        }

        "flatMap - 각 요소를 여러 개로 펼친 후 평탄화" {
            val nested: List<List<Int>> =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )

            val flattened: List<Int> = nested.flatMap { it }
            flattened shouldBe listOf(1, 2, 3, 4)
        }

        "flatMapIndexed - 인덱스와 함께 각 요소를 여러 개로 펼친 후 평탄화" {
            val nested: List<List<Int>> =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )

            val flattened: List<Int> =
                nested.flatMapIndexed { index, list ->
                    list.map { value -> index * value }
                }

            flattened shouldBe listOf(0, 0, 3, 4)
        }

        "flatMapTo - 중첩된 리스트를 평탄화해서 기존 컬렉션에 추가" {
            val source: List<List<Int>> =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )
            val destination: MutableList<Int> = mutableListOf(0)
            source.flatMapTo(destination) { it }

            destination shouldBe listOf(0, 1, 2, 3, 4)
        }

        "flatMapIndexed - 인덱스와 함께 각 요소를 여러 개로 펼친 후 평탄화해서 기존 컬렉션에 추가" {
            val nested: List<List<Int>> =
                listOf(
                    listOf(1, 2),
                    listOf(3, 4),
                )
            val destination: MutableList<Int> = mutableListOf(0, 1, 2)
            nested.flatMapIndexedTo(destination) { index, list ->
                list.map { value -> value * 2 }
            }

            destination shouldBe listOf(0, 1, 2, 2, 4, 6, 8)
        }
    }

    "Map 의 flattening" - {
        "flatMap - Map<K, List<V>> 구조를 평탄화" {
            val map: Map<String, List<Int>> =
                mapOf(
                    "a" to listOf(1, 2),
                    "b" to listOf(3),
                )

            val result: List<String> =
                map.flatMap { (key, numbers) ->
                    numbers.map { number -> "$key:$number" }
                }

            result shouldBe listOf<String>("a:1", "a:2", "b:3")
        }

        "flatMap - 사실 flatMap 의 람다 파라미터의 리턴 타입이 Iterable 타입이기만 하면 된다." {
            val map: Map<String, Int> =
                mapOf(
                    "a" to 1,
                    "b" to 2,
                    "c" to 3,
                )

            val result: List<Int> =
                map.flatMap { (_, number) ->
                    List(number) { number }
                }

            result shouldBe listOf<Int>(1, 2, 2, 3, 3, 3)
        }

        "toList - Map<K, V> 를 List<Pair<K, V>> 로 변환" {
            val map: Map<String, List<Int>> =
                mapOf(
                    "a" to listOf(1, 2),
                    "b" to listOf(3),
                )

            val toList: List<Pair<String, List<Int>>> = map.toList()

            toList shouldBe
                listOf<Pair<String, List<Int>>>(
                    "a" to listOf<Int>(1, 2),
                    "b" to listOf<Int>(3),
                )
        }

        "flatMapValues 메서드는 따로 없어서 Map<K, V>.values.flatten 사용" {
            val map: Map<String, List<Int>> =
                mapOf(
                    "a" to listOf(1, 2),
                    "b" to listOf(3),
                )

            val result: List<Int> = map.values.flatten()
            result shouldBe listOf<Int>(1, 2, 3)
        }

        "flatPairValues 메서드는 따로 없어서 아래처럼 재구성 필요" {
            val map: Map<String, List<Int>> =
                mapOf(
                    "a" to listOf(1, 2),
                    "b" to listOf(3),
                )

            val result1: List<Pair<String, Int>> =
                map.flatMap { entry ->
                    entry.value.map { value -> entry.key to value }
                }

            val result2: List<Pair<String, Int>> =
                map.entries.flatMap { entry ->
                    entry.value.map { value -> entry.key to value }
                }

            result1 shouldBe
                listOf<Pair<String, Int>>(
                    "a" to 1,
                    "a" to 2,
                    "b" to 3,
                )
            result1 shouldBe result2
        }

        "Map<String, String>.toList().flatten 은 단순 Key-Value Pair 리스트로 변환" {
            val map: Map<String, String> = mapOf()

            val flatList: List<Pair<String, String>> = map.toList()

            val flatMap: List<Pair<String, Char>> =
                map.flatMap { entry ->
                    entry.value.map { value -> entry.key to value }
                }

            flatList shouldBe flatMap
        }
    }
})
