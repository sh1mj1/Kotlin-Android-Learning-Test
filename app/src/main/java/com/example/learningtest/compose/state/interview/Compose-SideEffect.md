# 영어 표현으로 컴포즈 부수효과 배우기

## 상태(State)와 효과(Effect)

Compose에서 자주 등장하는 두 가지 핵심 개념:

| 개념              | 의미                                  | 예시                                        |
|-----------------|-------------------------------------|-------------------------------------------|
| **상태 (State)**  | UI에 반영되는 데이터. 변경되면 Recomposition 발생 | `mutableStateOf`, `remember`, `StateFlow` |
| **효과 (Effect)** | 상태 변경 외의 모든 작업. UI 렌더링과 무관한 부수 작업   | API 호출, 로깅, 리스너 등록                        |

```kotlin
@Composable
fun MyScreen() {
    // 상태: UI에 반영됨
    var count by remember { mutableStateOf(0) }

    // 효과: UI 렌더링 외의 부수 작업
    LaunchedEffect(count) {
        analytics.log("count changed: $count")
    }

    Text("$count")  // 상태를 UI로 표현
}
```

Compose는 **순수 함수**처럼 동작해야 하므로, 효과(Effect)는 반드시 `LaunchedEffect`, `SideEffect` 등 전용 API를 통해 실행해야 한다.

---

## launch (코루틴 빌더)

- 발사하다, 시작하다: 새로운 코루틴을 시작(launch) 하고, 결과를 기다리지 않고 바로 반환한다.
    - 반환값: `Job` (코루틴 제어용 핸들)
    - Fire-and-forget 방식 - 실행만 시키고 결과는 신경 쓰지 않음
    - `async`와 달리 값을 반환하지 않음

```kotlin
scope.launch {
    // 새 코루틴이 "발사"되어 독립적으로 실행됨
    delay(1000)
    println("완료!")
}
// launch 직후 바로 다음 줄 실행 (기다리지 않음)
```

---

## LaunchedEffect

- 실행된 부수효과: 컴포저블이 컴포지션될 때 코루틴 launch 를 실행한다.
    - key 가 바뀌면 이전 코루틴은 취소하고 새 코루틴을 launch
    - 컴포저블과 생명주기에 맞춰 자동으로 취소되며, 리컴포지션 시에는 다시 실행되지 않는다.
    - 일회성 이벤트(api 호출, 뷰모델에서 로드, 애니메이션 시작 등)에 적합하다.
    - 실습 예제: [LaunchedEffectScreen.kt](LaunchedEffectScreen.kt)

```kotlin
LaunchedEffect(userId) {
    // userId가 바뀔 때마다 새로 "발사"되어 실행됨
    val user = api.fetchUser(userId)
}
```

---

## remember (Compose 함수)

- 기억하다: Recomposition이 일어나도 값을 기억(유지) 해둔다.

```kotlin
// ❌ recomposition 될 때마다 0으로 초기화
var count = 0

// ✅ recomposition 되어도 값을 "기억"
var count by remember { mutableStateOf(0) }
```

---

## rememberCoroutineScope

- 코루틴 범위를 기억해두다: CoroutineScope를 기억해두고, 필요할 때 내가 직접 launch 한다.
    - 컴포저블 함수이지만, 반환된 scope는 onClick 등 이벤트 핸들러에서 사용 가능하다.
    - 코루틴의 Job을 저장해두고 수동으로 취소해야 할 때 사용한다.
    - 컴포지션 종료 시 scope가 자동 취소되므로 메모리 누수 걱정 없이 안전하게 사용 가능하다.
    - 코루틴 수동 관리 예시: 애니메이션 취소: [AnimationCancellationScreen.kt](AnimationCancellationScreen.kt)

LaunchedEffect는 key가 바뀔 때만 자동 취소되지만, 이렇게 사용자 이벤트로 직접 취소하려면 Job을 저장해두고 관리해야 한다.

### LaunchedEffect vs rememberCoroutineScope

| 구분 | LaunchedEffect        | rememberCoroutineScope |
|----|-----------------------|------------------------|
| 실행 | 자동 (Composition 진입 시) | 수동 (내가 직접 launch)      |
| 용도 | 화면 진입 시 API 호출        | 버튼 클릭 등 이벤트 대응         |
| 비유 | 자동문                   | 수동문 (손잡이를 기억해둠)        |

```kotlin
LaunchedEffect(Unit) {
    doSomething()
}

val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch { doSomething() }
})
```

---

## rememberUpdatedState

- 갱신된 상태를 기억해두다: 항상 최신(Updated) 값을 기억해둔다.
    - 실습 예제: [RememberUpdatedStateScreen.kt](RememberUpdatedStateScreen.kt)

### 핵심 정리

| 구분   | remember | rememberUpdatedState |
|------|----------|----------------------|
| 값 유지 | 처음 값 고정  | 항상 최신 값으로 갱신         |
| 용도   | 일반 상태 저장 | Effect 내에서 최신 값 참조   |

---

## DisposableEffect

- 일회용 부수효과(폐기 가능한, 폐기해야 하는 부수효과): 리소스를 사용하고 컴포저블이 사라질 때 Dispose 해야 하는 부수효과
    - onDispose 블록에서 cleanup 로직을 반드시 제공해야 한다.
    - key 변경 시 onDispose -> 재실행 순서로 동작
    - 리스너 등록 해제, 콜백 연결/해제 등 쌍으로 동작하는 작업.
    - 실습 예제: [DisposableEffectScreen.kt](DisposableEffectScreen.kt)

```kotlin
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { ... }
    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
```

---

## SideEffect

- 부수효과: Composition 완료 후 비-compose 코드에 부수효과를 전달한다.
    - 매 Recomposition 마다 실행된다. (key 없음)
    - 코루틴이 아닌 동기 코드만 실행 가능하다.
    - Compose 상태를 Compose 가 아닌 외부 시스템에 동기화할 때 사용한다.

```kotlin
SideEffect {
    // 매 recomposition 성공 후 실행 - Analytics 등 외부 시스템에 "부수적으로" 알림
    analytics.setUserProperty("userName", user.name)
}
```

---

## produceState

- "상태를 생산한다" - 비-Compose 데이터 소스(Flow, LiveData, 콜백 등)를 Compose State로 생산(변환) 한다.
    - 외부 데이터 → Compose State로의 변환기(converter) 역할
    - 초기값을 제공하고, 코루틴 내에서 `value`를 갱신하여 State를 "생산"
    - 내부적으로 LaunchedEffect + mutableStateOf를 조합한 것

```kotlin
val user by produceState<User?>(initialValue = null, userId) {
    // 외부 데이터를 State로 "생산"
    value = api.fetchUser(userId)
}
```

---

## derivedStateOf

- 파생된 상태: 하나 이상의 State로부터 파생(계산) 되는 새로운 State.
    - 원본 State가 변경될 때만 재계산된다
    - 불필요한 recomposition 방지에 효과적 (예: 리스트에서 특정 조건 필터링)
    - `remember { derivedStateOf { ... } }` 패턴으로 주로 사용

```kotlin
val highPriorityTasks by remember {
    derivedStateOf {
        // tasks에서 "파생된" 새로운 상태
        tasks.filter { it.priority == Priority.HIGH }
    }
}
```

---

## snapshotFlow

"Snapshot" = 스냅샷, 순간 포착 | "Flow" = 흐름

→ "스냅샷을 Flow로" - Compose State의 스냅샷(순간 값) 을 Kotlin Flow 스트림으로 변환한다.

- Compose State → Flow 변환 (produceState의 반대 방향)
- State 값이 변경될 때마다 Flow에 emit된다
- Compose 상태 변화를 Flow 연산자(debounce, filter 등)로 처리할 때 유용

```kotlin
LaunchedEffect(listState) {
    snapshotFlow { listState.firstVisibleItemIndex }
        .distinctUntilChanged()  // Flow 연산자 활용
        .collect { index ->
            // State의 "스냅샷"이 Flow로 흘러옴
            analytics.trackScrollPosition(index)
        }
}
```