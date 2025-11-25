---
name: compose-learning
description: "Creates Jetpack Compose learning tests for UI components (Composable, remember, State), recomposition (LaunchedEffect, DisposableEffect, SideEffect), and UI structure (Modifier, Layout). Generates complete, runnable Compose UI tests with ComposeTestRule in src/test/kotlin/learning/compose/"
---

# Compose Learning Test Generator

## Overview
This skill creates interactive learning tests for Jetpack Compose UI development. Tests are generated as complete, executable Kotest-based tests using Compose Test APIs that can be run immediately to verify understanding.

## Scope
This skill handles **Jetpack Compose** topics:
- **Compose Basics**: Composable functions, remember, State, derivedStateOf, mutableStateOf
- **Recomposition**: LaunchedEffect, DisposableEffect, SideEffect, rememberCoroutineScope, rememberUpdatedState
- **UI Structure**: Modifier, Layout composables, Custom composables, Slot APIs
- **State Management**: State hoisting, ViewModel integration, State vs MutableState
- **Testing**: ComposeTestRule, semantics, assertions, interactions

## Test Generation Strategy

### 1. Analyze Request
When the user requests a Compose learning test, identify:
- **Topic**: Which Compose feature/API to learn
- **Depth**: Basic usage, edge cases, or comparisons
- **Test Type**: Logic test (Kotest) or UI test (ComposeTestRule)

### 2. Create Test File
Generate test file in: `src/test/kotlin/learning/compose/[Topic]Test.kt`

**File naming convention:**
- Single concept: `RememberStateTest.kt`
- Comparison: `RememberVsRememberSaveableTest.kt`
- Category: `ComposeEffectsTest.kt`

### 3. Test Structure
Use Kotest `FunSpec` style with ComposeTestRule:

```kotlin
class [Topic]Test : FunSpec({
    test("[학습 포인트 설명]") {
        // given: ComposeTestRule로 UI 설정
        composeTestRule.setContent {
            // Composable 정의
        }

        // when: UI 상호작용 또는 상태 변경

        // then: UI 상태 검증 (semantics assertions)
        composeTestRule.onNodeWithText("Expected").assertExists()
    }
})
```

### 4. Learning Comments
Add educational comments explaining:
- **Why**: Why this Compose pattern is necessary
- **When**: When to use this pattern
- **Gotchas**: Common Compose mistakes to avoid

## Output Format

### Complete Test File
Generate a fully functional test file with:
1. **Package declaration**: `package learning.compose`
2. **Imports**: Kotest, Compose Test, Compose runtime
3. **Test class**: Extending `FunSpec`
4. **ComposeTestRule**: Using `createComposeRule()`
5. **Multiple test cases**: 3-7 test cases covering different aspects
6. **Learning comments**: Korean comments explaining key concepts
7. **Execution instructions**: How to run the test

### Example Output
For request: "remember와 rememberSaveable 차이 테스트 만들어줘"

**Generated file**: `src/test/kotlin/learning/compose/RememberVsRememberSaveableTest.kt`

```kotlin
package learning.compose

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * 학습 목표: remember와 rememberSaveable의 차이점 이해
 * - remember: recomposition 동안 값 유지 (configuration change 시 소멸)
 * - rememberSaveable: configuration change 후에도 값 유지 (Bundle에 저장)
 */
class RememberVsRememberSaveableTest : FunSpec({
    val composeTestRule = createComposeRule()

    test("remember는 recomposition 동안 상태를 유지한다") {
        // given
        var recomposeCount by mutableStateOf(0)
        var rememberedValue: Int? = null

        composeTestRule.setContent {
            val counter = remember { mutableStateOf(0) }
            rememberedValue = counter.value

            // when: recomposition 트리거
            LaunchedEffect(recomposeCount) {
                counter.value++
            }
        }

        // then: 초기값은 0
        rememberedValue shouldBe 0

        // when: recomposition
        recomposeCount++
        composeTestRule.waitForIdle()

        // then: 값이 유지되고 증가됨
        rememberedValue shouldBe 1
    }

    test("remember는 key가 변경되면 재초기화된다") {
        // given
        var key by mutableStateOf("key1")
        var currentValue: Int? = null

        composeTestRule.setContent {
            val value = remember(key) {
                // key가 변경될 때마다 새로운 값으로 초기화
                100
            }
            currentValue = value
        }

        // then: 초기값
        currentValue shouldBe 100

        // when: key 변경
        key = "key2"
        composeTestRule.waitForIdle()

        // then: 다시 초기화됨
        currentValue shouldBe 100
    }

    test("rememberSaveable은 Bundle에 저장 가능한 타입만 지원") {
        // given: String, Int, Boolean 등 기본 타입은 자동 저장
        composeTestRule.setContent {
            val stringState = rememberSaveable { mutableStateOf("Hello") }
            val intState = rememberSaveable { mutableStateOf(42) }
            val boolState = rememberSaveable { mutableStateOf(true) }

            // then: 모두 정상 동작
            stringState.value shouldBe "Hello"
            intState.value shouldBe 42
            boolState.value shouldBe true
        }
    }

    test("remember를 사용한 계산 결과 캐싱") {
        // given
        var sourceValue by mutableStateOf(10)
        var calculationCount = 0

        composeTestRule.setContent {
            // when: remember로 계산 결과 캐싱
            val expensiveResult = remember(sourceValue) {
                calculationCount++
                sourceValue * 2
            }

            // 동일한 sourceValue에서는 재계산 안 함
        }

        // then: 한 번만 계산됨
        calculationCount shouldBe 1

        // when: 같은 값으로 recompose
        composeTestRule.waitForIdle()

        // then: 재계산 안 됨
        calculationCount shouldBe 1

        // when: 다른 값으로 변경
        sourceValue = 20
        composeTestRule.waitForIdle()

        // then: 재계산됨
        calculationCount shouldBe 2
    }
})

/**
 * 실행 방법:
 * 1. 터미널: ./gradlew test --tests RememberVsRememberSaveableTest
 * 2. IDE: 클래스 좌측 실행 버튼 클릭
 *
 * 학습 포인트:
 * - remember: recomposition 동안만 상태 유지 (앱 회전 시 소멸)
 * - rememberSaveable: configuration change 후에도 유지 (Bundle 저장)
 * - remember(key): key 변경 시 재초기화 (계산 캐싱에 유용)
 * - remember는 성능 최적화용 (불필요한 재계산 방지)
 */
```

## Usage Instructions

### When This Skill Activates
The skill automatically activates when you request:
- "Compose remember 테스트 만들어줘"
- "LaunchedEffect 사용법 학습"
- "Modifier 테스트 생성"
- "State hoisting 패턴 테스트"
- Any Jetpack Compose UI or state management learning request

### What You Provide
1. **Topic**: What to learn (e.g., "remember", "LaunchedEffect", "Modifier")
2. **Optional depth**: "기초", "심화", "비교" etc.

### What You Get
1. **Complete test file** in correct location
2. **Runnable Compose tests** with ComposeTestRule
3. **Learning comments** in Korean
4. **Execution instructions**
5. **Key takeaways** summary

## Dependencies Required
Ensure your `build.gradle.kts` includes:

```kotlin
dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.runtime:runtime")

    // Testing
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

## Best Practices

### 1. Use ComposeTestRule for UI Interactions
```kotlin
composeTestRule.setContent { /* Composable */ }
composeTestRule.onNodeWithText("Button").performClick()
composeTestRule.onNodeWithTag("TextField").assertExists()
```

### 2. Test State Changes
Focus on how state changes trigger recomposition:
```kotlin
test("State 변경 시 recomposition") {
    var state by mutableStateOf("Initial")

    composeTestRule.setContent {
        Text(text = state)
    }

    state = "Updated"
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithText("Updated").assertExists()
}
```

### 3. Test Side Effects
Verify LaunchedEffect, DisposableEffect behavior:
```kotlin
test("LaunchedEffect는 key 변경 시 재실행") {
    var key by mutableStateOf(1)
    var launchCount = 0

    composeTestRule.setContent {
        LaunchedEffect(key) {
            launchCount++
        }
    }

    key = 2
    composeTestRule.waitForIdle()

    launchCount shouldBe 2
}
```

### 4. Add Educational Comments
Every test should explain the Compose concept being demonstrated.

### 5. Use Semantics for Testing
Prefer semantic properties over implementation details:
```kotlin
// ✅ Good: semantic-based
composeTestRule.onNodeWithText("Submit").performClick()

// ❌ Bad: implementation-based
// (Avoid relying on Compose internals)
```

## Troubleshooting

### If Test Directory Doesn't Exist
Create the directory structure:
```bash
mkdir -p app/src/test/kotlin/learning/compose
```

### If Compose Test Not Configured
Add Compose test dependencies to `app/build.gradle.kts` and sync.

### If UI Tests Fail
Ensure `ui-test-manifest` is in `debugImplementation` for test manifests.

## Related Skills
- **kotlin-learning**: For pure Kotlin/coroutines tests (no Compose)
- **android-learning**: For Android framework tests (ViewModel, Navigation)
- **learning-progress**: To track completed Compose learning topics
