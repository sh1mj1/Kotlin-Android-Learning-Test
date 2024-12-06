package com.example.learningtest.nestedAndInner

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class NestedAndInnerClassBasicTest : FreeSpec({
    "In kotlin" - {
        "nested class is default" - {
            "can create nested instance without outer instance" {
                shouldNotThrow<Exception> { OuterForNested.Nested() }
            }
            "is defined inside another class." {
                OuterForNested.Nested()::class.java.isMemberClass shouldBe true
            }
            "not hold a reference to an instance of the Outer class" {
                OuterForNested.Nested().nestedFunction() shouldBe ReferenceAvailable.CAN_NOT_ACCESS
            }
        }
        "inner class for inner keyword" - {
            "create inner class with outer class's instance" {
                shouldNotThrow<Exception> { OuterForInner().Inner() }
            }
            "is defined inside another class." {
                OuterForInner().Inner()::class.java.isMemberClass shouldBe true
            }
            "hold a reference to an instance of the Outer class" {
                OuterForInner().Inner().innerFunction() shouldBe ReferenceAvailable.CAN_ACCESS
            }
        }
    }
    "In java" - {
        "inner class is default" - {
            "create inner class with outer class's instance" {
                shouldNotThrow<Exception> { OuterForInnerJava().Inner() }
            }
            "is defined inside another class." {
                OuterForInnerJava().Inner()::class.java.isMemberClass shouldBe true
            }
            "hold a reference to an instance of the Outer class" {
                OuterForInnerJava().Inner().innerFunction() shouldBe ReferenceAvailable.CAN_ACCESS
            }
        }
        "nested class for static keyword" - {
            "create nested class with outer class's instance" {
                shouldNotThrow<Exception> { OuterForNestedJava.Nested() }
            }
            "is defined inside another class." {
                OuterForNestedJava.Nested()::class.java.isMemberClass shouldBe true
            }
            "hold a reference to an instance of the Outer class" {
                OuterForNestedJava.Nested()
                    .nestedFunction() shouldBe ReferenceAvailable.CAN_NOT_ACCESS
            }
        }
    }
})
