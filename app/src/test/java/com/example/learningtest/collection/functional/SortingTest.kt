package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeSorted
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldBeSortedDescending
import io.kotest.matchers.collections.shouldBeSortedDescendingBy
import io.kotest.matchers.collections.shouldBeSortedWith
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

        "sortedWith 는 Comparator 를 사용하여 정렬" - {
            "sortedWith - 기본" {
                val users: List<User> =
                    listOf(
                        User("Alice", 30),
                        User("Cathy", 22),
                        User("Bob", 28),
                    )
                val sortedByAge1: List<User> = users.sortedWith(compareBy<User> { it.age })
                val sortedByAge2: List<User> =
                    users.sortedWith { user1, user2 ->
                        user1.age.compareTo(user2.age)
                    }
                val sortedByAge3: List<User> =
                    users.sortedWith { user1, user2 ->
                        user1.age - user2.age
                    }

                val expected: List<User> =
                    listOf(
                        User("Cathy", 22),
                        User("Bob", 28),
                        User("Alice", 30),
                    )

                sortedByAge1 shouldBe expected
                sortedByAge2 shouldBe expected
                sortedByAge3 shouldBe expected

                sortedByAge1.shouldBeSortedWith(compareBy<User> { it.age })
                sortedByAge2.shouldBeSortedWith(compareBy<User> { it.age })
                sortedByAge3.shouldBeSortedWith(compareBy<User> { it.age })
            }

            "sortedWith - 이름이 같을 경우 입력 순서 유지 (Stable Sort)" {
                val users: List<User> =
                    listOf(
                        User("Alice", 30),
                        User("Bob", 25),
                        User("Alice", 22),
                    )
                val sortedByName1: List<User> = users.sortedWith(compareBy<User> { it.name })
                val sortedByName2: List<User> =
                    users.sortedWith { user1, user2 ->
                        user1.name.compareTo(user2.name)
                    }
                /* String 타입의 비교는 - 연산으로 불가능하다.
                val sortedByName3 : List<User> = users.sortedWith { user1, user2 ->
                    user1.name - user2.name
                }
                 */

                val expected: List<User> =
                    listOf(
                        User("Alice", 30),
                        User("Alice", 22),
                        User("Bob", 25),
                    )

                sortedByName1 shouldBe expected
                sortedByName2 shouldBe expected

                sortedByName1 shouldBeSortedWith (compareBy<User> { it.name })
                sortedByName2 shouldBeSortedWith (compareBy(User::name))
            }

            "sortedWith + compareByDescending" {
                val users: List<User> =
                    listOf(
                        User("Alice", 30),
                        User("Cathy", 22),
                        User("Bob", 28),
                    )

                val sortedByDescendingAge1: List<User> =
                    users.sortedWith(
                        compareByDescending<User> {
                            it.age
                        },
                    )
                val sortedByDescendingAge2: List<User> =
                    users.sortedWith { user1, user2 ->
                        user2.age.compareTo(user1.age)
                    }
                val sortedByDescendingAge3: List<User> =
                    users.sortedWith { user1, user2 ->
                        user2.age - user1.age
                    }

                val expected: List<User> =
                    listOf(
                        User("Alice", 30),
                        User("Bob", 28),
                        User("Cathy", 22),
                    )

                sortedByDescendingAge1 shouldBe expected
                sortedByDescendingAge2 shouldBe expected
                sortedByDescendingAge3 shouldBe expected

                sortedByDescendingAge1 shouldBeSortedWith (compareByDescending(User::age))
                sortedByDescendingAge2 shouldBeSortedWith (compareByDescending(User::age))
                sortedByDescendingAge3 shouldBeSortedWith (compareByDescending(User::age))
            }

            "sortedWith - 이름으로 오름차순 정렬, 이름이 같다면 나이로 오름차순 정렬" {
                val users: List<User> =
                    listOf(
                        User("Alice", 30),
                        User("Cathy", 22),
                        User("Alice", 28),
                    )

                val sorted1: List<User> =
                    users.sortedWith(compareBy<User> { it.name }.thenBy { it.age })
                val sorted2: List<User> = users.sortedWith(compareBy(User::name).thenBy(User::age))
                val sorted3: List<User> =
                    users.sortedWith { user1, user2 ->
                        user1.name.compareTo(user2.name)
                    }
            }
        }

        "reversed - 순서만 뒤집기 (뒤집으면서 정렬하지는 않음)" {
            val original: List<Int> = listOf(1, 3, 2)
            val reversed: List<Int> = original.reversed()

            reversed shouldBe listOf<Int>(2, 3, 1)
        }
    }
})
