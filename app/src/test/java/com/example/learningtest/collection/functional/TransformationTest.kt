package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TransformationTest : FreeSpec({
    "List Transformation" - {
        "distinct - 중복 제거" {
            val list: List<Int> = listOf(1, 2, 3, 1, 2, 4, 5, 4)
            list.distinct() shouldBe listOf<Int>(1, 2, 3, 4, 5)
        }

        "distinctBy - 특정 조건에 따라 중복 제거" {
            data class Person(val name: String, val age: Int)

            val people: List<Person> =
                listOf(
                    Person("Alice", 30),
                    Person("Bob", 25),
                    Person("Alice", 35),
                    Person("Charlie", 30),
                    Person("Bob", 40),
                )

            people.distinctBy(Person::name) shouldBe
                listOf<Person>(
                    Person("Alice", 30),
                    Person("Bob", 25),
                    Person("Charlie", 30),
                )

            people.distinctBy(Person::age) shouldBe
                listOf<Person>(
                    Person("Alice", 30),
                    Person("Bob", 25),
                    Person("Alice", 35),
                    Person("Bob", 40),
                )
        }

        "zip - 두 컬렉션을 Pair 로 묶기(크기가 작은 컬렉션에 맞춰서 Pair 가 생성됨)" {
            val numbers: List<Int> = listOf(1, 2, 3)
            val letters: List<String> = listOf("a", "b", "c", "d")

            val zipped: List<Pair<Int, String>> = numbers.zip(letters)
            zipped shouldBe listOf(1 to "a", 2 to "b", 3 to "c")
        }

        "zipWithNext - 인접한 요소끼리 Pair 로 묶기" {
            val numbers: List<Int> = listOf(1, 2, 3)

            val adjacentPairs: List<Pair<Int, Int>> = numbers.zipWithNext()
            adjacentPairs shouldBe listOf(1 to 2, 2 to 3)
        }

        "windowed - 슬라이딩 윈도우를 만들고 각 윈도우에 대한 리스트 반환" {
            val numbers = listOf(1, 2, 3, 4, 5, 6)

            // windowed(size = 3, step = 1, partialWindows = false)
            numbers.windowed(3) shouldBe
                listOf(
                    listOf(1, 2, 3),
                    listOf(2, 3, 4),
                    listOf(3, 4, 5),
                    listOf(4, 5, 6),
                )

            // windowed(size = 3, step = 2, partialWindows = false)
            numbers.windowed(3, step = 2) shouldBe
                listOf(
                    listOf(1, 2, 3),
                    listOf(3, 4, 5),
                )

            // windowed(size = 3, step = 2, partialWindows = true)
            numbers.windowed(3, step = 2, partialWindows = true) shouldBe
                listOf(
                    listOf(1, 2, 3),
                    listOf(3, 4, 5),
                    listOf(5, 6),
                )

            // (1,2,3), (2,3,4), (3,4,5), (4,5,6) => 6, 9, 12, 15
            numbers.windowed(3) { it.sum() } shouldBe listOf(6, 9, 12, 15)
        }

        "chunked - 컬렉션을 지정된 크기의 덩어리로 나누기" {
            val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7)

            numbers.chunked(3) shouldBe
                listOf<List<Int>>(
                    listOf(1, 2, 3),
                    listOf(4, 5, 6),
                    listOf(7),
                )
            numbers.windowed(size = 3, step = 3, partialWindows = true) shouldBe numbers.chunked(3)

            numbers.chunked(size = 3, transform = { it.sum() }) shouldBe listOf(6, 15, 7)
            numbers.chunked(3) { it.sum() } shouldBe listOf(6, 15, 7)
        }

        "partition - 조건을 만족하는 요소와 만족하지 않는 요소로 분리" {
            val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)

            val (even, odd) = numbers.partition { it % 2 == 0 }

            even shouldBe listOf(2, 4, 6)
            odd shouldBe listOf(1, 3, 5)
        }

        "unzip - Pair를 요소로 갖는 리스트를 두 개의 리스트로 분리" {
            val pairs = listOf("a" to 1, "b" to 2, "c" to 3)

            val (letters, numbers) = pairs.unzip()

            letters shouldBe listOf("a", "b", "c")
            numbers shouldBe listOf(1, 2, 3)
        }
    }

    "Set Transformation" - {
        val set: Set<Int> = setOf(1, 2, 3, 4)

        "zip - 두 Set을 Pair로 묶기" {
            val set1: Set<String> = setOf("a", "b", "c")
            val set2: Set<Int> = setOf(1, 2, 3, 4)

            set1.zip(set2) shouldBe listOf("a" to 1, "b" to 2, "c" to 3)
        }

        "zipWithNext - 인접한 요소끼리 Pair로 묶기" {
            set.zipWithNext() shouldBe listOf(1 to 2, 2 to 3, 3 to 4)
        }

        "windowed - 슬라이딩 윈도우를 만들고 각 윈도우에 대한 리스트 반환" {
            set.windowed(2) shouldBe listOf(listOf(1, 2), listOf(2, 3), listOf(3, 4))
        }

        "chunked - 컬렉션을 지정된 크기의 덩어리로 나누기" {
            set.chunked(3) shouldBe listOf(listOf(1, 2, 3), listOf(4))
        }

        "partition - 조건을 만족하는 요소와 만족하지 않는 요소로 분리" {
            val sets: Set<Int> = setOf(1, 2, 3, 4, 5, 6)
            val (even: List<Int>, odd: List<Int>) = sets.partition { it % 2 == 0 }
            even shouldBe listOf(2, 4, 6)
            odd shouldBe listOf(1, 3, 5)
        }

        "unzip - Pair를 요소로 갖는 Set을 두 개의 Set로 분리" {
            val sets: Set<Pair<Int, Int>> = setOf(1 to 2, 3 to 4, 5 to 6)
            val (set1: List<Int>, set2: List<Int>) = sets.unzip()
            set1 shouldBe listOf(1, 3, 5)
            set2 shouldBe listOf(2, 4, 6)
        }
    }

    "Map Transformation" - {
        val map: Map<String, Int> = mapOf("a" to 1, "b" to 2, "c" to 3)

        "mapKeys - Key에 변환 함수 적용하여 새로운 Map 생성" {
            val transformedKeysMap = map.mapKeys { (key, _) -> key.uppercase() }
            transformedKeysMap shouldBe mapOf("A" to 1, "B" to 2, "C" to 3)
        }

        "mapValues - Value에 변환 함수 적용하여 새로운 Map 생성" {
            val transformedValuesMap = map.mapValues { (_, value) -> value * 10 }
            transformedValuesMap shouldBe mapOf("a" to 10, "b" to 20, "c" to 30)
        }

        "entries, keys, values - Map의 구성 요소를 Collection으로 변환" {
            map.entries shouldBe
                setOf(
                    mapOf("a" to 1).entries.first(),
                    mapOf("b" to 2).entries.first(),
                    mapOf("c" to 3).entries.first(),
                )
            map.keys shouldBe setOf("a", "b", "c")
            map.values shouldBe listOf(1, 2, 3)
        }
    }
})
