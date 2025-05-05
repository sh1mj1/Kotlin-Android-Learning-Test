package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CombinationTest : FreeSpec({
    "List Combination" - {
        "plus (+) - 두 컬렉션을 이어붙이거나 요소 하나를 추가" {
            val list1 = listOf(1, 3)
            val list2 = listOf(2, 4)

            list1 + list2 shouldBe listOf(1, 3, 2, 4)

            list1 + 2 shouldBe listOf(1, 3, 2)
//            (2 + list1) 처럼 (원소 + 리스트)는 불가능
        }

        "minus (-) - 다른 컬렉션이나 특정 요소를 제거" {
            val list1 = listOf(1, 2, 3, 2)
            val list2 = listOf(1, 4)

            list1 - list2 shouldBe listOf(2, 3, 2)
            list1 - 1 shouldBe listOf(2, 3, 2)
        }

        "union - 두 컬렉션의 합집합 (중복 제거)" {
            val list1 = listOf<Int>(1, 2, 3)
            val list2 = listOf<Int>(3, 4, 5)

            (list1 union list2) shouldBe setOf<Int>(1, 2, 3, 4, 5)
        }

        "intersect - 두 컬렉션의 교집합 (중복 제거)" {
            val list1 = listOf(1, 2, 3, 4, 2)
            val list2 = listOf(3, 4, 5, 4)

            (list1 intersect list2) shouldBe setOf(3, 4)
        }

        "subtract - 첫 번째 컬렉션에서 두 번째 컬렉션의 요소를 제외한 차집합 (중복 제거)" {
            val list1 = listOf<Int>(1, 2, 3, 4, 2)
            val list2 = listOf<Int>(3, 4, 5, 4)

            (list1 subtract list2) shouldBe setOf(1, 2) // list1 에서의 중복도 제거됨.
        }
    }

    "Map Combination" - {
        val map1 = mapOf("a" to 1, "b" to 2)
        val map2 = mapOf("b" to 20, "c" to 30)

        "plus (+) " {
            map1 + map2 shouldBe mapOf("a" to 1, "b" to 20, "c" to 30)
        }

        "merge - 키 존재 시 값 결합, 키 부재 시 추가" {
            val mutableMap = mutableMapOf("a" to 1, "b" to 2)

            mutableMap.merge("c", 10) { oldValue, newValue ->
                oldValue + newValue
            }

            mutableMap shouldBe mapOf("a" to 1, "b" to 2, "c" to 10)

            mutableMap.merge("a", 10) { oldValue, newValue ->
                oldValue + newValue
            }

            mutableMap shouldBe mapOf("a" to 11, "b" to 2, "c" to 10)
        }

        "entries, keys, values 를 이용한 union, intersect, subtract (기존 테스트 유지)" - {
            "entries 를 이용한 union (결과 Set)" {
                val unionResult: Set<Map.Entry<String, Int>> = map1.entries union map2.entries
                unionResult shouldBe
                    setOf(
                        mapOf("a" to 1).entries.first(),
                        mapOf("b" to 2).entries.first(),
                        mapOf("b" to 20).entries.first(),
                        mapOf("c" to 30).entries.first(),
                    )
            }

            "entries 를 이용한 intersect (결과 Set)" {
                val intersectResult: Set<Map.Entry<String, Int>> =
                    map1.entries intersect map2.entries

                intersectResult shouldBe emptySet()

                val map3 = mapOf("a" to 1, "b" to 2)
                val map4 = mapOf("a" to 1, "b" to 4)

                val intersectResult2: Set<Map.Entry<String, Int>> =
                    map3.entries intersect map4.entries

                intersectResult2 shouldBe setOf(mapOf("a" to 1).entries.first())
            }

            "keys 를 이용한 union (결과 Set)" {
                val unionResult: Set<String> = map1.keys union map2.keys
                unionResult shouldBe setOf("a", "b", "c")
            }

            "keys 를 이용한 intersect (결과 Set)" {
                val intersectResult: Set<String> = map1.keys intersect map2.keys
                intersectResult shouldBe setOf("b")
            }

            "keys 를 이용한 subtract (결과 Set)" {
                val subtractResult: Set<String> = map1.keys subtract map2.keys
                subtractResult shouldBe setOf("a")
            }

            "values 를 이용한 조합 union (결과 Set)" {
                val unionResult: Set<Int> = map1.values union map2.values

                unionResult shouldBe setOf(1, 2, 20, 30)
            }
        }
    }
})
