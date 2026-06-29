---
name: kotlin-learning
description: "Creates comprehensive Kotlin learning tests for language features (collections, lambdas, extensions, sealed classes), coroutines (launch, async, Job, Dispatcher), and Flow (operators, StateFlow, SharedFlow). Generates complete, runnable tests with Kotest in src/test/kotlin/learning/kotlin/"
---

# Kotlin Learning Test Generator

## Overview
This skill creates interactive learning tests for Kotlin programming language features, coroutines, and Flow. Tests are generated as complete, executable Kotest-based unit tests that can be run immediately to verify understanding.

## Scope
This skill handles **pure Kotlin** topics without Android dependencies:
- **Kotlin Language**: Collections, lambdas, extensions, sealed classes, data classes, delegation, inline functions
- **Coroutines**: launch, async, Job, Dispatcher, CoroutineScope, structured concurrency, exception handling
- **Flow**: cold/hot flows, operators (map, filter, collect), StateFlow, SharedFlow, channelFlow

## Test Generation Strategy

### 1. Analyze Request
When the user requests a learning test, identify:
- **Topic**: What Kotlin feature/API to learn
- **Depth**: Basic usage, edge cases, or comparisons
- **Focus**: Single concept or multiple related concepts

### 2. Create Test File
Generate test file in: `src/test/kotlin/learning/kotlin/[Topic]Test.kt`

**File naming convention:**
- Single concept: `FlowMapOperatorTest.kt`
- Comparison: `LaunchVsAsyncTest.kt`
- Category: `KotlinCollectionsTest.kt`

### 3. Test Structure
Use Kotest `FunSpec` style with Given-When-Then structure:

```kotlin
class [Topic]Test : FunSpec({
    test("[학습 포인트 설명]") {
        // given: 테스트 데이터 준비

        // when: 테스트할 동작 실행

        // then: 결과 검증 (Kotest matchers)
        result shouldBe expected
    }
})
```

### 4. Learning Comments
Add educational comments explaining:
- **Why**: Why this behavior occurs
- **When**: When to use this feature
- **Gotchas**: Common mistakes to avoid

## Output Format

### Complete Test File
Generate a fully functional test file with:
1. **Package declaration**: `package learning.kotlin`
2. **Imports**: All necessary imports (Kotest, coroutines, etc.)
3. **Test class**: Extending `FunSpec`
4. **Multiple test cases**: 3-7 test cases covering different aspects
5. **Learning comments**: Korean comments explaining key concepts
6. **Execution instructions**: How to run the test

### Example Output
For request: "코루틴 launch와 async 차이 테스트 만들어줘"

**Generated file**: `src/test/kotlin/learning/kotlin/LaunchVsAsyncTest.kt`

```kotlin
package learning.kotlin

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.*

/**
 * 학습 목표: launch와 async의 차이점 이해
 * - launch: 결과를 반환하지 않는 비동기 작업 (Job 반환)
 * - async: 결과를 반환하는 비동기 작업 (Deferred 반환)
 */
class LaunchVsAsyncTest : FunSpec({

    test("launch는 Job을 반환한다") {
        // given
        val scope = CoroutineScope(Dispatchers.Default)

        // when
        val job = scope.launch { delay(100) }

        // then: Job은 결과 값을 가지지 않음
        job shouldBeInstanceOf Job::class
        job.join() // 완료 대기만 가능
    }

    test("async는 Deferred를 반환한다") {
        // given
        val scope = CoroutineScope(Dispatchers.Default)

        // when
        val deferred = scope.async {
            delay(100)
            "결과 값"
        }

        // then: Deferred는 await()로 결과 값을 받을 수 있음
        deferred shouldBeInstanceOf Deferred::class
        deferred.await() shouldBe "결과 값"
    }

    test("launch는 fire-and-forget 방식으로 사용") {
        // when: 결과가 필요 없는 작업에 사용
        val job = CoroutineScope(Dispatchers.Default).launch {
            println("로그 기록")
            delay(50)
            println("백그라운드 작업 완료")
        }

        // then: Job으로 제어만 가능
        job.isActive shouldBe true
        job.cancel()
        job.isCancelled shouldBe true
    }

    test("async는 결과가 필요한 작업에 사용") {
        // when: 여러 비동기 작업의 결과를 조합
        val scope = CoroutineScope(Dispatchers.Default)
        val deferred1 = scope.async { delay(50); 10 }
        val deferred2 = scope.async { delay(50); 20 }

        // then: 두 결과를 합산
        val result = deferred1.await() + deferred2.await()
        result shouldBe 30
    }
})

/**
 * 실행 방법:
 * 1. 터미널에서: ./gradlew test --tests LaunchVsAsyncTest
 * 2. IDE에서: 클래스 좌측 실행 버튼 클릭
 *
 * 학습 포인트:
 * - launch: 결과가 필요 없는 비동기 작업 (로깅, 알림, 백그라운드 작업)
 * - async: 결과가 필요한 비동기 작업 (네트워크 요청, 계산, 데이터 조합)
 * - Deferred는 Job을 상속하므로, async도 Job의 기능을 모두 사용 가능
 */
```

## Usage Instructions

### When This Skill Activates
The skill automatically activates when you request:
- "코루틴 테스트 만들어줘"
- "Flow collect 사용법 학습"
- "sealed class 테스트 생성"
- "람다와 고차함수 차이점"
- Any Kotlin language feature, coroutines, or Flow learning request

### What You Provide
1. **Topic**: What to learn (e.g., "코루틴 launch", "Flow operators")
2. **Optional depth**: "기초", "심화", "비교" etc.

### What You Get
1. **Complete test file** in correct location
2. **Runnable tests** with Kotest assertions
3. **Learning comments** in Korean
4. **Execution instructions**
5. **Key takeaways** summary

## Dependencies Required
Ensure your `build.gradle.kts` includes:

```kotlin
dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

## Best Practices

### 1. Focus on Learning Objectives
Each test should teach **one clear concept**. Don't mix multiple unrelated topics.

### 2. Use Kotest Matchers
Prefer readable matchers:
- `result shouldBe expected` over `assertEquals(expected, result)`
- `list shouldHaveSize 3` over `assertEquals(3, list.size)`
- `value.shouldBeInstanceOf<Type>()` for type checks

### 3. Add Educational Comments
Every test should have comments explaining:
- **What** is being tested
- **Why** it behaves this way
- **When** to use this pattern

### 4. Provide Complete Examples
Tests should be self-contained and runnable without additional setup.

### 5. Follow Kotlin Naming Conventions
Test names use backtick style with Korean descriptions:
```kotlin
test("launch는 Job을 반환한다") { ... }
```

## Troubleshooting

### If Test Directory Doesn't Exist
Create the directory structure:
```bash
mkdir -p app/src/test/kotlin/learning/kotlin
```

### If Kotest Not Configured
Add dependencies to `app/build.gradle.kts` and sync project.

### If Coroutines Test Fails
Ensure `kotlinx-coroutines-test` dependency is added.

## Related Skills
- **compose-learning**: For Jetpack Compose UI tests
- **android-learning**: For Android framework tests
- **learning-progress**: To track completed learning topics
