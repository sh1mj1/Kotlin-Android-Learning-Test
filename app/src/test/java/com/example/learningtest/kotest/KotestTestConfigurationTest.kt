package com.example.learningtest.kotest

import io.kotest.core.spec.style.FunSpec

class KotestTestConfigurationTest : FunSpec({
    beforeSpec {
        println("Before the spec. This will be printed only once")
    }

    afterSpec {
        println("After the spec. This will be printed only once")
    }

    beforeTest {
        println("Before each test. This will be printed before each test")
    }
    afterTest {
        println("After each test. This will be printed after each test")
    }

    test("Test 1") {
        println("Test 1")
    }
    test("Test 2") {
        println("Test 2")
    }
})
