package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeSorted
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldBeSortedDescending
import io.kotest.matchers.collections.shouldBeSortedDescendingBy
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.shouldBe
import java.util.SortedMap

class SortingTest : FreeSpec({
    "List Sorting" - {
        data class User(val name: String, val age: Int)

        "정렬하여 새로운 컬렉션 생성" - {
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
                    val sortedByAge1: List<User> = users.sortedWith(compareBy(User::age))
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

                "sortedWith - 나이로 정렬하되 다른 필드가 모두 같을 경우 입력 순서 유지 (Stable Sort)" {
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
                    val sorted2: List<User> =
                        users.sortedWith { user1, user2 ->
                            val nameComparison = user1.name.compareTo(user2.name)
                            if (nameComparison != 0) {
                                return@sortedWith nameComparison
                            }
                            return@sortedWith user1.age.compareTo(user2.age)
                        }

                    val expected: List<User> =
                        listOf(
                            User("Alice", 28),
                            User("Alice", 30),
                            User("Cathy", 22),
                        )

                    sorted1 shouldBe expected
                    sorted2 shouldBe expected
                }

                "sortedWith - 이름으로 오름차순 정렬, 이름이 같다면 나이로 내림차순 정렬" {
                    val users: List<User> =
                        listOf(
                            User("Alice", 30),
                            User("Cathy", 22),
                            User("Alice", 28),
                        )

                    val sorted1: List<User> =
                        users.sortedWith(compareBy<User> { it.name }.thenByDescending { it.age })
                    val sorted2: List<User> =
                        users.sortedWith { user1, user2 ->
                            val nameComparison = user1.name.compareTo(user2.name)
                            if (nameComparison != 0) {
                                return@sortedWith nameComparison
                            }
                            return@sortedWith user2.age.compareTo(user1.age)
                        }

                    val expected: List<User> =
                        listOf(
                            User("Alice", 30),
                            User("Alice", 28),
                            User("Cathy", 22),
                        )

                    sorted1 shouldBe expected
                    sorted2 shouldBe expected
                }

                "naturalOrder() - Comparable 인터페이스를 구현하는 타입의 자연스러운 오름차순 순서에 따라 정렬하는 Comparator" {
                    data class User(
                        val name: String,
                        val age: Int,
                    ) : Comparable<User> {
                        override fun compareTo(other: User): Int =
                            compareBy(User::name)
                                .thenBy(User::age)
                                .compare(this, other)
                    }

                    val users: List<User> =
                        listOf(
                            User("Alice", 30),
                            User("Cathy", 22),
                            User("Alice", 28),
                        )

                    // sortedWith(naturalOrder()) ==  sorted()
                    val sorted1 = users.sortedWith(naturalOrder())
                    val sorted2 = users.sorted()

                    val expected: List<User> =
                        listOf(
                            User("Alice", 28),
                            User("Alice", 30),
                            User("Cathy", 22),
                        )
                    sorted1 shouldBe expected
                    sorted2 shouldBe expected
                }

                "reversedOrder() - Comparable 인터페이스를 구현하는 타입의 자연스러운 순서의 역순에 따라 정렬하는 Comparator" {
                    data class User(
                        val name: String,
                        val age: Int,
                    ) : Comparable<User> {
                        override fun compareTo(other: User): Int =
                            compareBy(User::name)
                                .thenBy(User::age)
                                .compare(this, other)
                    }

                    val users: List<User> =
                        listOf(
                            User("Alice", 30),
                            User("Cathy", 22),
                            User("Alice", 28),
                        )

                    // sortedWith(reversedOrder()) ==  sortedDescending()
                    val sorted1 = users.sortedWith(reverseOrder())
                    val sorted2 = users.sortedDescending()

                    val expected: List<User> =
                        listOf(
                            User("Cathy", 22),
                            User("Alice", 30),
                            User("Alice", 28),
                        )
                    sorted1 shouldBe expected
                    sorted2 shouldBe expected
                }
            }

            "reversed - 순서만 뒤집기 (뒤집으면서 정렬하지는 않음)" {
                val original: List<Int> = listOf(1, 3, 2)
                val reversed: List<Int> = original.reversed()

                reversed shouldBe listOf<Int>(2, 3, 1)
            }
        }

        "기존 Mutable 컬렉션을 정렬" - {
            "sort" {
                val original: MutableList<Int> = mutableListOf(3, 1, 2)
                original.sort()

                original shouldBe listOf<Int>(1, 2, 3)
            }

            "sortDescending" {
                val original: MutableList<Int> = mutableListOf(3, 1, 2)
                original.sortDescending()

                original shouldBe listOf<Int>(3, 2, 1)
            }

            "sortBy - MutableList 특정 속성 기준 제자리 정렬" {
                data class User(val name: String, val age: Int)

                val users: MutableList<User> =
                    mutableListOf(
                        User("A", 30),
                        User("B", 20),
                        User("C", 25),
                    )
                users.sortBy { it.age }
                users shouldBe
                    listOf<User>(
                        User("B", 20),
                        User("C", 25),
                        User("A", 30),
                    )
            }

            "sortWith - MutableList Comparator 사용 제자리 정렬" {
                data class User(val name: String, val age: Int)

                val users: MutableList<User> =
                    mutableListOf(
                        User("Alice", 30),
                        User("Cathy", 22),
                        User("Bob", 28),
                    )
                users.sortWith(compareBy { it.age }) // 원본 리스트가 변경됨
                users shouldBe
                    listOf<User>(
                        User("Cathy", 22),
                        User("Bob", 28),
                        User("Alice", 30),
                    )
            }

            "reverse - 순서만 뒤집기 (뒤집으면서 정렬하지는 않음)" {
                val original: MutableList<Int> = mutableListOf(1, 3, 2)
                original.reverse()

                original shouldBe listOf<Int>(2, 3, 1)
            }
        }
    }

    "Map Sorting" - {
        "toSortedMap - 키를 기준으로 오름차순 정렬하여 SortedMap 생성" {
            val map: Map<String, Int> = mapOf("c" to 3, "a" to 1, "b" to 2)
            val sortedMap: SortedMap<String, Int> = map.toSortedMap()

            sortedMap shouldBe mapOf<String, Int>("a" to 1, "b" to 2, "c" to 3)
        }

        "sortedBy - Map의 값을 기준으로 오름차순 정렬하여 List<Map.Entry<K, V>> 생성" {
            val map: Map<String, Int> = mapOf("c" to 3, "a" to 1, "b" to 2)

            val sortedList: List<Map.Entry<String, Int>> =
                map.entries.sortedBy(Map.Entry<String, Int>::value)

            sortedList.map(Map.Entry<String, Int>::value) shouldBe listOf<Int>(1, 2, 3)
            sortedList.map(Map.Entry<String, Int>::key) shouldBe listOf<String>("a", "b", "c")
        }

        "sortedWith - Map의 키를 기준으로 내림차순 정렬하여 List<Map.Entry<K, V>> 생성" {
            val map: Map<String, Int> = mapOf("c" to 3, "a" to 1, "b" to 2)
            val sortedList: List<Map.Entry<String, Int>> =
                map.entries.sortedWith(compareByDescending { it.key })

            sortedList.map(Map.Entry<String, Int>::key) shouldBe listOf<String>("c", "b", "a")
            sortedList.map(Map.Entry<String, Int>::value) shouldBe listOf<Int>(3, 2, 1)
        }
    }
})
