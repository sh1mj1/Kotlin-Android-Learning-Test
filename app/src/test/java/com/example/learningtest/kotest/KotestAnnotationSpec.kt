package com.example.learningtest.kotest

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

/**
 * If you are migrating from JUnit then AnnotationSpec is a spec that uses annotations like JUnit 4/5.
 *
 * Just add the @Test annotation to any function defined in the spec class.
 *
 * JUnit 에서 마이그레이션하는 경우 AnnotationSpec 은 JUnit 4/5 와 같이 애노테이션을 사용하는 스펙입니다.
 *
 * 스펙 클래스에 정의된 모든 함수에 @Test 애노테이션을 추가하면 됩니다.
 */
class KotestAnnotationSpec : AnnotationSpec() {
    @BeforeEach
    fun setUp() {
        println("Before each test")
    }

    @Test
    fun test1() {
        1 shouldBe 1
    }

    @Test
    fun test2() {
        3 shouldBe 3
    }
}
