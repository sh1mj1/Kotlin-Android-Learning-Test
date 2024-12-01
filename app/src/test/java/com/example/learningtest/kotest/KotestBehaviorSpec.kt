package com.example.learningtest.kotest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

/**
 * Popular with people who like to write tests in the BDD style.
 *
 * BehaviorSpec allows you to use `context`, `given`, `when`, `then`.
 */
class KotestBehaviorSpec : BehaviorSpec({
    context("a broomstick should be able to be fly and come back on it's own") {
        given("a broomstick") {
            `when`("I sit on it") {
                then("I should be able to fly") {
                    val broomStick = BroomStick()
                    broomStick.sitOn()
                    broomStick.canFly shouldBe true
                }
            }
            `when`("I throw it away") {
                then("it should come back") {
                    val broomStick = BroomStick()
                    broomStick.throwAway()
                    broomStick.hasReturned() shouldBe true
                }
            }
        }
    }
})

private class BroomStick {
    private var _canFly: Boolean = false
    val canFly: Boolean
        get() = _canFly

    private var _isReturning: Boolean = false
    val isReturning: Boolean
        get() = _isReturning

    fun sitOn() {
        _canFly = true
    }

    fun throwAway() {
        _isReturning = true
    }

    fun hasReturned(): Boolean = isReturning
}
