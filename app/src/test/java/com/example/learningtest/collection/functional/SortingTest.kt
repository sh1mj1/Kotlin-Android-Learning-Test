package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeSorted
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldBeSortedDescending
import io.kotest.matchers.collections.shouldBeSortedDescendingBy
import io.kotest.matchers.shouldBe

class SortingTest : FreeSpec({
    "List Sorting" - {
        data class User(val name: String, val age: Int)

        "sorted - 오름차순 정렬" {
            val numbers: List<Int> = listOf(3, 1, 4, 2)
            val sorted: List<Int> = numbers.sorted()

            sorted shouldBe listOf<Int>(1, 2, 3, 4)
            sorted.shouldBeSorted()
        }

        "sortedDescending - 내림차순 정렬" {
            val numbers: List<Int> = listOf(3, 1, 4, 2)
            val sortedDescending: List<Int> = numbers.sortedDescending()

            sortedDescending shouldBe listOf<Int>(4, 3, 2, 1)
            sortedDescending.shouldBeSortedDescending()
        }

        "sortedBy - 특정 속성 기준 정렬" {
            val users: List<User> =
                listOf(
                    User("A", 30),
                    User("B", 20),
                    User("C", 25),
                )
            val sorted: List<User> = users.sortedBy { it.age }

            sorted shouldBe
                listOf<User>(
                    User("B", 20),
                    User("C", 25),
                    User("A", 30),
                )
            sorted shouldBeSortedBy { it.age }
        }

        "sortedByDescending - 특정 속성 기준 내림차순 정렬" {
            val users: List<User> =
                listOf(
                    User("A", 30),
                    User("B", 20),
                    User("C", 25),
                )
            val sorted: List<User> = users.sortedByDescending { it.age }

            sorted shouldBe
                listOf<User>(
                    User("A", 30),
                    User("C", 25),
                    User("B", 20),
                )
            sorted shouldBeSortedDescendingBy { it.age }
        }

        "reversed - 순서만 뒤집기 (뒤집으면서 정렬하지는 않음)" {
            val original: List<Int> = listOf(1, 3, 2)
            val reversed: List<Int> = original.reversed()

            reversed shouldBe listOf<Int>(2, 3, 1)
        }
    }
})
