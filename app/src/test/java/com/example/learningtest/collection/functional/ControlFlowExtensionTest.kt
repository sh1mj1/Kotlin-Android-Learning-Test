package com.example.learningtest.collection.functional

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ControlFlowExtensionTest : FreeSpec({

    data class Person(var name: String, var age: Int, var city: String)

    "Scope Functions (스코프 함수) - 객체 컨텍스트에서 코드 블록 실행" - {

        "let - 객체를 it 으로 참조, 람다 결과 반환" {
            val person = Person("Alice", 20, "Amsterdam")
            val result: String =
                person.let {
                    it.age += 1
                    "Name: ${it.name}, Age: ${it.age}"
                }
            result shouldBe "Name: Alice, Age: 21"
            person.age shouldBe 21
        }

        "run - 객체를 this로 참조, 람다 결과 반환 (Non-extension run은 객체 없이 사용)" {
            val person = Person("Bob", 25, "London")
            val result: String =
                person.run {
                    this.city = "Paris"
                    age = 26
                    "Age: $age, City: $city"
                }
            result shouldBe "Age: 26, City: Paris"
            person.city shouldBe "Paris"
        }

        "with - 객체를 첫 번째 인자로, 객체를 this로 참조, 람다 결과 반환 (확장 함수 아님)" {
            val person = Person("Charlie", 30, "New York")
            val result: String =
                with(person) {
                    age += 5
                    "New Age: $age"
                }
            result shouldBe "New Age: 35"
            person.age shouldBe 35
        }

        "apply - 객체를 this로 참조, 객체 자체 반환 (주로 객체 초기화/설정)" {
            val person: Person =
                Person("David", 35, "Tokyo").apply {
                    age += 2
                    city = "Seoul"
                }

            person.apply {
                age += 2
            }

            person.age shouldBe 39
            person.city shouldBe "Seoul"
        }

        "also - 객체를 it 으로 참조, 객체 자체 반환 (주로 부수 효과 로깅 등)" {
            val numbers = mutableListOf(1, 2, 3)
            val resultList: MutableList<Int> =
                numbers.also {
                    it.add(4)
                    println("List after adding element: $it")
                }
            resultList shouldBe listOf(1, 2, 3, 4)
            numbers shouldBe listOf(1, 2, 3, 4)
        }
    }

    "Conditional Execution Functions (조건부 실행 함수) - 조건에 따라 객체 반환" - {

        "takeIf - 조건이 true 이면 객체 자체 반환, false 이면 null 반환" {
            val number = 10
            val evenNumber: Int? = number.takeIf { it % 2 == 0 }
            val oddNumber: Int? = number.takeIf { it % 2 != 0 }

            evenNumber shouldBe 10
            oddNumber shouldBe null
        }

        "takeUnless - 조건이 false 이면 객체 자체 반환, true 이면 null 반환" {
            val number = 10
            val notOddNumber = number.takeUnless { it % 2 != 0 }
            val notEvenNumber = number.takeUnless { it % 2 == 0 }

            notOddNumber shouldBe 10
            notEvenNumber shouldBe null
        }
    }

    "Combining Scope and Conditional Functions - 스코프 함수와 조건부 함수 조합" - {
        "takeIf 와 let 을 함께 사용하여 조건 만족 시에만 코드 실행" {
            val person: Person? = Person("Grace", 22, "Sydney")

            val result1 =
                person?.takeIf { it.age >= 18 }?.let {
                    "Adult: ${it.name}"
                }
            result1 shouldBe "Adult: Grace"

            val anotherPerson: Person? = Person("Heidi", 17, "Vienna")
            val result2 =
                anotherPerson?.takeIf { it.age >= 18 }?.let {
                    "Adult: ${it.name}"
                }
            result2 shouldBe null // 조건 불만족 시 takeIf가 null 반환하고 let 실행 안됨
        }

        "takeUnless 와 run 을 함께 사용하여 조건 불만족 시에만 코드 실행" {
            val person: Person? = Person("Ivy", 28, "Moscow")

            val result1 =
                person?.takeUnless { it.age < 18 }?.run {
                    "Not a minor: ${this.name}"
                }
            result1 shouldBe "Not a minor: Ivy"

            val anotherPerson: Person? = Person("Jack", 15, "Cairo")
            val result2 =
                anotherPerson?.takeUnless { it.age < 18 }?.run {
                    "Not a minor: ${this.name}"
                }
            result2 shouldBe null // 조건 만족 시 takeUnless가 null 반환하고 run 실행 안됨
        }
    }
})
