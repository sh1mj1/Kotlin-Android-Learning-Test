package com.example.learningtest.dataclass

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain

@DisplayName("Data class")
class TestDataClass : FreeSpec({
    "Data class" - {
        "should have value-based equals and hashCode" {
            val person1 = PersonData("sh1mj1", 29)
            val person2 = PersonData("sh1mj1", 29)

            person1.hashCode() shouldBe person2.hashCode()
            person1 shouldBe person2
        }
        "toString method" - {
            val person = PersonData("sh1mj1", 29)
            "should have a meaningful toString with primary properties" {
                person.toString() shouldBe "PersonData(name=sh1mj1, age=29)"
            }
            "should not have a toString with non-primary properties" {
                person.toString() shouldNotContain "isAdult"
            }
        }
        "should have a meaningful toString method" {
            val person = PersonData("sh1mj1", 29)
            person.toString() shouldBe "PersonData(name=sh1mj1, age=29)"
        }
        "copy method" - {
            val original = PersonData("sh1mj1", 29)
            val copy = original.copy(name = "sh1mj2")
            "should be able to copy with modifications" {
                copy shouldBe PersonData("sh1mj2", 29)
            }
            "should not modify the original instance" {
                original shouldBe PersonData("sh1mj1", 29)
            }
        }
        "should support destructuring declarations" {
            val (name, age) = PersonData("sh1mj1", 29)
            name shouldBe "sh1mj1"
            age shouldBe 29
        }
    }
    "Normal class(Not data class)" - {
        "should not have value-based equals and hashCode" {
            val person1 = PersonNotData("sh1mj1", 29)
            val person2 = PersonNotData("sh1mj1", 29)

            person1.hashCode() shouldNotBe person2.hashCode()
            person1 shouldNotBe person2
        }
        "toString method" - {
            "should have a toString method with class name and hash code" {
                val person = PersonNotData("sh1mj1", 29)
                person.toString() shouldContain "PersonNotData@"
                person.toString() shouldNotContain "name=sh1mj1"
                person.toString() shouldNotContain "age=29"
            }
        }
    }
})

private data class PersonData(val name: String, val age: Int) {
    @Suppress("unused")
    val isAdult: Boolean
        get() = age >= 20
}

private class PersonNotData(
    @Suppress("unused") val name: String,
    val age: Int,
) {
    @Suppress("unused")
    val isAdult: Boolean
        get() = age >= 20
}
