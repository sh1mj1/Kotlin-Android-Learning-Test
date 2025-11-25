# Compose Learning Reference Guide

## 학습 커리큘럼

### Level 1: Compose 기초
Jetpack Compose의 핵심 개념

#### 1.1 Composable Functions
- `@Composable` 어노테이션
- Composable의 생명주기
- Composition vs Recomposition
- Composable 함수 규칙 (순서 보장 안 됨, 병렬 실행 가능)

**테스트 예시**: "Composable 기초 테스트 만들어줘"

#### 1.2 State Management
- `mutableStateOf` - 상태 생성
- `remember` - recomposition 동안 상태 유지
- `rememberSaveable` - configuration change 후에도 유지
- `derivedStateOf` - 파생 상태 (계산된 상태)
- State vs MutableState

**테스트 예시**:
- "remember vs rememberSaveable 비교 테스트"
- "derivedStateOf 사용법 테스트"

#### 1.3 Recomposition
- Recomposition 트리거 조건
- Recomposition scope (최소 범위만 재구성)
- Smart recomposition (변경된 부분만)
- Recomposition 최적화 (불변 객체, stable types)

**테스트 예시**: "Recomposition 동작 원리 테스트"

#### 1.4 Modifier
- Modifier 체인 순서의 중요성
- 자주 사용되는 Modifier (padding, size, background, clickable)
- Modifier.then() - 조건부 modifier
- Custom modifier 생성

**테스트 예시**: "Modifier 순서 차이 테스트"

---

### Level 2: Side Effects
Compose에서 부수 효과 다루기

#### 2.1 LaunchedEffect
- Key 기반 재실행
- Coroutine scope 제공
- 용도: API 호출, 타이머, Flow collect
- 취소 및 재시작 동작

**테스트 예시**: "LaunchedEffect key 변경 시 재실행 테스트"

#### 2.2 DisposableEffect
- Composable이 화면에서 사라질 때 cleanup
- onDispose callback
- 용도: 리스너 등록/해제, 리소스 정리
- Key 기반 재실행

**테스트 예시**: "DisposableEffect cleanup 테스트"

#### 2.3 SideEffect
- Recomposition 성공 시마다 실행
- 용도: Compose 외부 상태 동기화
- 매 recomposition마다 실행 (성능 주의)

**테스트 예시**: "SideEffect 실행 시점 테스트"

#### 2.4 rememberCoroutineScope
- Composable 외부에서 코루틴 실행
- 이벤트 핸들러에서 suspend 함수 호출
- Composition이 남아있는 동안만 유효

**테스트 예시**: "rememberCoroutineScope 사용 테스트"

#### 2.5 rememberUpdatedState
- 최신 값 참조 유지 (캡처 방지)
- LaunchedEffect와 함께 사용
- 콜백에서 최신 상태 접근

**테스트 예시**: "rememberUpdatedState 캡처 방지 테스트"

---

### Level 3: Advanced Patterns
실전 Compose 패턴

#### 3.1 State Hoisting
- State를 상위 Composable로 올리기
- Stateless vs Stateful Composable
- 단방향 데이터 플로우 (UDF)
- ViewModel과 통합

**테스트 예시**: "State hoisting 패턴 테스트"

#### 3.2 Slot APIs
- Content lambda로 UI 커스터마이징
- 여러 slot 제공 (topBar, bottomBar 등)
- 재사용 가능한 컴포넌트 설계

**테스트 예시**: "Slot API 커스터마이징 테스트"

#### 3.3 Custom Layouts
- Layout Composable 생성
- Measurable과 Placeable
- 커스텀 레이아웃 로직
- SubcomposeLayout

**테스트 예시**: "Custom Layout 구현 테스트"

#### 3.4 Performance Optimization
- `remember`로 재계산 방지
- `key()` Composable로 안정성 보장
- Immutable/Stable annotation
- 람다 재생성 방지 (remember + callback)

**테스트 예시**: "Compose 성능 최적화 테스트"

#### 3.5 Testing Compose
- ComposeTestRule 사용
- Semantics 기반 테스팅
- UI 상호작용 시뮬레이션
- State 변경 검증

**테스트 예시**: "Compose UI 테스팅 패턴 학습"

---

## Compose 테스트 작성 패턴

### Pattern 1: State Change Test
State 변경이 UI에 반영되는지 검증

```kotlin
class StateChangeTest : FunSpec({
    val composeTestRule = createComposeRule()

    test("State 변경 시 Text가 업데이트된다") {
        // given
        var text by mutableStateOf("Initial")

        composeTestRule.setContent {
            Text(text = text)
        }

        // when
        text = "Updated"
        composeTestRule.waitForIdle()

        // then
        composeTestRule.onNodeWithText("Updated").assertExists()
        composeTestRule.onNodeWithText("Initial").assertDoesNotExist()
    }
})
```

### Pattern 2: Recomposition Scope Test
특정 부분만 recompose되는지 검증

```kotlin
class RecompositionScopeTest : FunSpec({
    val composeTestRule = createComposeRule()

    test("부모 state 변경 시 자식만 recompose") {
        // given
        var parentRecomposeCount = 0
        var childRecomposeCount = 0
        var childState by mutableStateOf(0)

        composeTestRule.setContent {
            ParentComposable {
                SideEffect { parentRecomposeCount++ }

                ChildComposable(childState) {
                    SideEffect { childRecomposeCount++ }
                }
            }
        }

        // when: 자식 state만 변경
        childState = 1
        composeTestRule.waitForIdle()

        // then: 자식만 recompose
        childRecomposeCount shouldBe 2 // initial + update
        parentRecomposeCount shouldBe 1 // initial only
    }
})
```

### Pattern 3: Side Effect Lifecycle Test
Side effect가 올바른 시점에 실행/정리되는지 검증

```kotlin
class SideEffectLifecycleTest : FunSpec({
    val composeTestRule = createComposeRule()

    test("LaunchedEffect는 key 변경 시 취소되고 재실행") {
        // given
        var key by mutableStateOf(1)
        var launchCount = 0
        var cancelCount = 0

        composeTestRule.setContent {
            LaunchedEffect(key) {
                launchCount++
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    if (!isActive) cancelCount++
                }
            }
        }

        // when: key 변경
        key = 2
        composeTestRule.waitForIdle()

        // then: 이전 effect 취소되고 새로 실행
        launchCount shouldBe 2
        cancelCount shouldBe 1
    }
})
```

### Pattern 4: Modifier Order Test
Modifier 순서에 따른 동작 차이 검증

```kotlin
class ModifierOrderTest : FunSpec({
    val composeTestRule = createComposeRule()

    test("padding 순서에 따라 클릭 영역이 달라진다") {
        // given
        var clicked1 = false
        var clicked2 = false

        composeTestRule.setContent {
            Column {
                // Case 1: clickable -> padding
                Box(
                    Modifier
                        .clickable { clicked1 = true }
                        .padding(16.dp)
                ) { Text("Box1") }

                // Case 2: padding -> clickable
                Box(
                    Modifier
                        .padding(16.dp)
                        .clickable { clicked2 = true }
                ) { Text("Box2") }
            }
        }

        // when: Box1의 padding 영역 클릭
        // then: clicked1 = true (padding도 클릭 가능)

        // when: Box2의 padding 영역 클릭
        // then: clicked2 = false (padding은 클릭 불가)
    }
})
```

---

## ComposeTestRule 사용법

### UI 설정
```kotlin
composeTestRule.setContent {
    MyComposable()
}
```

### UI 요소 찾기
```kotlin
// 텍스트로 찾기
composeTestRule.onNodeWithText("Submit")

// Tag로 찾기 (testTag modifier 필요)
composeTestRule.onNodeWithTag("LoginButton")

// Content description으로 찾기
composeTestRule.onNodeWithContentDescription("Profile Image")

// 조건으로 찾기
composeTestRule.onNode(hasClickAction())
```

### UI 상호작용
```kotlin
// 클릭
composeTestRule.onNodeWithText("Button").performClick()

// 텍스트 입력
composeTestRule.onNodeWithTag("TextField").performTextInput("Hello")

// 스크롤
composeTestRule.onNodeWithTag("LazyColumn").performScrollToIndex(10)

// 제스처
composeTestRule.onNodeWithTag("Draggable").performGesture {
    swipeLeft()
}
```

### UI 검증
```kotlin
// 존재 확인
composeTestRule.onNodeWithText("Title").assertExists()

// 존재하지 않음 확인
composeTestRule.onNodeWithText("Error").assertDoesNotExist()

// 표시 여부
composeTestRule.onNodeWithTag("Dialog").assertIsDisplayed()

// 활성화 여부
composeTestRule.onNodeWithText("Submit").assertIsEnabled()

// 선택 여부
composeTestRule.onNodeWithTag("Checkbox").assertIsSelected()
```

### 대기
```kotlin
// Compose가 idle 상태가 될 때까지 대기
composeTestRule.waitForIdle()

// 특정 조건이 true가 될 때까지 대기
composeTestRule.waitUntil(timeoutMillis = 5000) {
    // condition
}
```

---

## Kotest + ComposeTestRule 통합

### Setup
```kotlin
class MyComposeTest : FunSpec({
    // ComposeTestRule 생성
    val composeTestRule = createComposeRule()

    test("테스트 설명") {
        composeTestRule.setContent {
            // UI 설정
        }

        // 테스트 로직
    }
})
```

### 여러 테스트에서 공통 UI 사용
```kotlin
class MyComposeTest : FunSpec({
    val composeTestRule = createComposeRule()

    beforeEach {
        composeTestRule.setContent {
            MyComposable()
        }
    }

    test("테스트 1") {
        // UI는 beforeEach에서 이미 설정됨
    }

    test("테스트 2") {
        // UI는 beforeEach에서 이미 설정됨
    }
})
```

---

## 일반적인 실수와 해결법

### 실수 1: remember 없이 mutableStateOf 사용
```kotlin
// ❌ Bad: 매 recomposition마다 새로운 State 객체 생성
@Composable
fun BadCounter() {
    val count = mutableStateOf(0) // 항상 0으로 리셋됨!
}

// ✅ Good: remember로 State 객체 유지
@Composable
fun GoodCounter() {
    val count = remember { mutableStateOf(0) }
}
```

### 실수 2: LaunchedEffect에서 State 직접 캡처
```kotlin
// ❌ Bad: 초기값이 캡처됨 (callback 실행 시 outdated)
@Composable
fun BadTimer(callback: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(5000)
        callback() // 이 callback은 초기 값
    }
}

// ✅ Good: rememberUpdatedState로 최신 값 유지
@Composable
fun GoodTimer(callback: () -> Unit) {
    val currentCallback by rememberUpdatedState(callback)
    LaunchedEffect(Unit) {
        delay(5000)
        currentCallback() // 항상 최신 callback
    }
}
```

### 실수 3: Modifier 순서 무시
```kotlin
// ❌ Bad: clickable 후 size → 작은 클릭 영역
Box(
    Modifier
        .clickable { }
        .size(100.dp)
)

// ✅ Good: size 후 clickable → 큰 클릭 영역
Box(
    Modifier
        .size(100.dp)
        .clickable { }
)
```

### 실수 4: DisposableEffect 없이 리스너 등록
```kotlin
// ❌ Bad: Composable이 제거되어도 리스너 남음 (메모리 누수)
@Composable
fun BadListener() {
    LaunchedEffect(Unit) {
        listener.register()
        // cleanup 없음!
    }
}

// ✅ Good: DisposableEffect로 cleanup 보장
@Composable
fun GoodListener() {
    DisposableEffect(Unit) {
        listener.register()
        onDispose {
            listener.unregister()
        }
    }
}
```

### 실수 5: 불필요한 recomposition
```kotlin
// ❌ Bad: 매번 새로운 람다 → 매번 recomposition
@Composable
fun BadButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) { // 새 람다 객체
        Text("Click")
    }
}

// ✅ Good: onClick를 그대로 전달
@Composable
fun GoodButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Click")
    }
}
```

---

## 추천 학습 경로

### 초보자 경로 (1주)
**Day 1-2**: Composable & State
1. Composable 함수 기초
2. mutableStateOf와 remember
3. State 변경과 recomposition

**Day 3-4**: Side Effects 기초
1. LaunchedEffect 사용법
2. DisposableEffect cleanup
3. rememberCoroutineScope

**Day 5-7**: Modifier & Layout
1. Modifier 체인 순서
2. 자주 사용되는 Modifier
3. Row, Column, Box

### 중급자 경로 (1주)
**Day 1-2**: Recomposition 최적화
1. Recomposition scope
2. remember + key
3. derivedStateOf

**Day 3-4**: State Hoisting
1. Stateless Composable 패턴
2. ViewModel 통합
3. 단방향 데이터 플로우

**Day 5-7**: Testing
1. ComposeTestRule 기초
2. Semantics 기반 테스팅
3. UI 상호작용 시뮬레이션

### 고급자 경로 (1주)
**Day 1-2**: Custom Composables
1. Slot API 패턴
2. Custom Modifier
3. 재사용 가능한 컴포넌트

**Day 3-4**: Performance
1. Stable/Immutable annotation
2. key() Composable
3. 성능 프로파일링

**Day 5-7**: Advanced Layout
1. Custom Layout
2. SubcomposeLayout
3. Intrinsic measurements

---

## 유용한 참고 자료

### 공식 문서
- [Jetpack Compose 공식 문서](https://developer.android.com/jetpack/compose)
- [Compose State 가이드](https://developer.android.com/jetpack/compose/state)
- [Side Effects 가이드](https://developer.android.com/jetpack/compose/side-effects)

### 학습 자료
- [Compose Pathway](https://developer.android.com/courses/pathways/compose)
- [Compose Codelab](https://developer.android.com/codelabs/jetpack-compose-basics)
- [Compose Samples](https://github.com/android/compose-samples)

### 테스팅 가이드
- [Compose Testing 공식 문서](https://developer.android.com/jetpack/compose/testing)
- [Testing Cheatsheet](https://developer.android.com/jetpack/compose/testing-cheatsheet)
