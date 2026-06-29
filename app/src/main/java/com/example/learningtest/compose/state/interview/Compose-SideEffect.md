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
    - 코루틴의 Job을 저장해두고 코루틴 스코프에서 실행했다가 원할 때 취소하고 싶을 때 사용한다. 
    - 컴포지션 종료 시 코루틴 스코프가 자동 취소된다.
    - 코루틴 수동 관리 예시: 애니메이션 취소: [AnimationCancellationScreen.kt](AnimationCancellationScreen.kt)

TODO: 코루틴 스코프 공부를 다시 해야겠다.
TODO: 리컴포지션 시에는 어떻게 되지? 

---

## rememberUpdatedState

- 갱신된 상태를 기억해두다: 항상 최신(Updated) 값을 기억해둔다.
    - 실습 예제: [RememberUpdatedStateScreen.kt](RememberUpdatedStateScreen.kt)

TODO: 업데이트 상태 기억. 

---

## DisposableEffect

- 일회용 부수효과(폐기 가능한, 폐기해야 하는 부수효과): 리소스를 사용하고 컴포저블이 사라질 때 Dispose 해야 하는 부수효과
    - onDispose 블록에서 cleanup 로직을 반드시 제공해야 한다.
    - key 변경 시 onDispose -> 재실행 순서로 동작
    - 리스너 등록 해제, 콜백 연결/해제 등 쌍으로 동작하는 작업.
    - 실습 예제: [DisposableEffectScreen.kt](DisposableEffectScreen.kt)


---

## SideEffect

- 부수효과: Composition 완료 후 비-compose 코드에 부수효과를 전달한다.
    - 매 Recomposition 마다 실행된다. (key 없음)
    - 코루틴이 아닌 동기 코드만 실행 가능하다.
    - Compose 상태를 Compose 가 아닌 외부 시스템에 동기화할 때 사용한다.
    - 실습 예제: [SideEffectScreen.kt](SideEffectScreen.kt)

```kotlin
SideEffect {
    // 매 recomposition 성공 후 실행 - Analytics 등 외부 시스템에 "부수적으로" 알림
    analytics.setUserProperty("userName", user.name)
}
```

---

## produceState

- 상태를 생산한다: 비컴포즈 데이터 소스를 컴포즈 상태로 변환
  - 초기값 제공. 코루틴 내에서 value 를 갱신.
  - 내부적으로 LaunchedEffect + mutableStateOf 조합. 

- "상태를 생산한다" - 비-Compose 데이터 소스(Flow, LiveData, 콜백 등)를 Compose State로 생산(변환) 한다.
    - 초기값을 제공하고, 코루틴 내에서 `value`를 갱신하여 State를 "생산"
    - 내부적으로 LaunchedEffect + mutableStateOf를 조합한 것


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

- 실제 구현 간소화한 버전으로 알아보기

```kotlin
interface State<out T> {
  val value: T
}

private class DerivedState<T>(
  private val calculation: () -> T
) : State<T> {

  private var currentValue: T? = null
  private var isValid = false

  // 의존하는 State들 추적
  private val dependencies = mutableSetOf<State<*>>()

  override val value: T
      get() {
          // 1. 의존성이 변경되었는지 확인
          if (!isValid || dependenciesChanged()) {
              // 2. 재계산 필요 → calculation 실행
              currentValue = calculation()
              isValid = true
              updateDependencies()
          }

          // 3. 캐시된 값 반환
          return currentValue!!
      }

  private fun dependenciesChanged(): Boolean {
      // 의존하는 State 중 하나라도 변경되었는지 확인
      return dependencies.any { it.hasChanged() }
  }

  private fun updateDependencies() {
      // calculation 실행 중 접근한 State 추적
      dependencies.clear()
      // ... tracking logic
  }
}

fun <T> derivedStateOf(calculation: () -> T): State<T> {
  return DerivedState(calculation)
}
```

- 핵심 매커니즘
1. Snapshot System (의존성 추적)
   Compose 는 Snapshot System 을 사용하여 자동으로 의존성을 추적한다.
```kotlin
val count by remember { mutableSetOf(0) }
val isEven by remember {
    derivedStateOf {
        println("계산 중...")
        count % 2 == 0 // count 읽기 -> 의존성 등록
    }
}
```
내부 동작: 
1. derivedStateOf 블록 실행 시
2. count.value 접근 -> Snapshot System 이 감지
3. "isEven 은 count 에 의존한다" 자동 등록.
4. count 변경 시 -> isEven 무효화 (isValid = false)

Snapshot System 의 마법
- 명시적으로 의존성을 선언할 필요 없음.
- 블록 내에서 읽은 모든 State 를 자동 추적.
- React 의 useMemo 의존성 배열과 달리 자동.




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