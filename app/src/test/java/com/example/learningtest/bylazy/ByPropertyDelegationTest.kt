package com.example.learningtest.bylazy

import com.example.learningtest.util.captureOutput
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.util.Locale
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@DisplayName("Property Delegation")
class ByPropertyDelegationTest : FreeSpec({
    "Simple case: Delegate getter & setter" - {
        "delegate getter to Delegate class" {
            val greetings = Greetings()
            greetings.content shouldBe "thank you for delegating 'content' to me!"
        }
        "delegate setter to Delegate class" {
            val greetings = Greetings()
            val output =
                captureOutput {
                    greetings.content = "Hello"
                }
            output shouldBe "Hello has been assigned to 'content'."
        }
    }

    "Lazy Delegate case: by lazy" - {
        "do not need to initialize the property manually" {
            val user = User("Alice")
            user.greeting shouldBe "Hello, Alice!"
        }

        "should not recompute after initialization" {
            val user = User("Bob")
            val firstGreeting = user.greeting
            val secondGreeting = user.greeting
            firstGreeting shouldBe "Hello, Bob!"
            secondGreeting shouldBe "Hello, Bob!"
            firstGreeting shouldBe secondGreeting
        }
    }

    "Observable Delegate case: by Delegates.observable" - {
        "observe property changes" {
            val person = Person("Alice")
            val output =
                captureOutput {
                    person.name = "Bob"
                }
            output shouldBe "OBSERVE: Property 'name' changed from 'Alice' to 'Bob'"
        }
    }

    "Custom Delegate case: by ScreamingFormat implementing ReadWriteProperty" - {
        "capitalize every letters of the property" {
            class Person {
                var name: String by ScreamingFormat()
            }
            val person = Person()
            person.name = "  alice"
            person.name shouldBe "ALICE"
        }
    }
})

class Greetings {
    var content: String by Delegate()
}

class Delegate {
    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): String = "thank you for delegating '${property.name}' to me!"

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: String,
    ) {
        println("$value has been assigned to '${property.name}'.")
    }
}

data class User(val name: String) {
    val greeting: String by lazy {
        println("Computing greeting...")
        "Hello, $name!"
    }
}

class Person(name: String) {
    var name: String by Delegates.observable(name) { prop, old, new ->
        println("OBSERVE: Property '${prop.name}' changed from '$old' to '$new'")
    }
}

class ScreamingFormat : ReadWriteProperty<Any?, String> {
    private var formattedValue: String = ""

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): String {
        return formattedValue
    }

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: String,
    ) {
        formattedValue = value.trim().uppercase(Locale.getDefault())
    }
}
