# Kotlin Learning Reference Guide

## 학습 커리큘럼

### Level 1: Kotlin 기초
학습 순서대로 나열된 테스트 주제

#### 1.1 Collections
- List, Set, Map 기본 사용법
- `map`, `filter`, `reduce` 변환 연산
- `fold`, `groupBy`, `partition` 고급 연산
- Sequence vs Collection 성능 차이
- Mutable vs Immutable 컬렉션

**테스트 예시**: "Kotlin collection 연산 학습 테스트 만들어줘"

#### 1.2 Lambda & Higher-Order Functions
- 람다 표현식 문법
- 고차 함수 (함수를 파라미터로 받는 함수)
- `it` vs 명시적 파라미터
- 람다 리시버 (with, apply, also, let, run)
- 인라인 함수와 성능 최적화

**테스트 예시**: "람다와 고차함수 테스트 만들어줘"

#### 1.3 Extension Functions
- 확장 함수 정의 및 사용
- 확장 프로퍼티
- Nullable receiver 확장
- 확장 함수 vs 멤버 함수 우선순위

**테스트 예시**: "확장 함수 학습 테스트 만들어줘"

#### 1.4 Data Classes & Sealed Classes
- Data class 자동 생성 기능 (equals, hashCode, toString, copy)
- Component functions (destructuring)
- Sealed class vs Enum class
- When expression exhaustiveness

**테스트 예시**: "sealed class 활용 테스트 만들어줘"

#### 1.5 Delegation
- Class delegation (`by` keyword)
- Property delegation (`by lazy`, `observable`)
- Custom delegates

**테스트 예시**: "Kotlin delegation 패턴 테스트 만들어줘"

---

### Level 2: Coroutines 기초
비동기 프로그래밍의 핵심 개념

#### 2.1 Coroutine Basics
- `suspend` 함수
- `runBlocking` - 코루틴을 블로킹 방식으로 실행
- `launch` - Fire-and-forget 비동기 작업
- `async/await` - 결과를 반환하는 비동기 작업
- `Job` - 코루틴 제어 인터페이스
- `Deferred` - 미래의 값을 나타내는 Job

**테스트 예시**:
- "코루틴 기초 테스트 만들어줘"
- "launch vs async 비교 테스트 만들어줘"

#### 2.2 CoroutineScope & Dispatchers
- `CoroutineScope` - 코루틴의 생명주기 관리
- `Dispatchers.Default` - CPU 집약적 작업
- `Dispatchers.IO` - I/O 작업
- `Dispatchers.Main` - UI 작업 (Android)
- `Dispatchers.Unconfined` - 특정 스레드 없음
- Custom dispatchers

**테스트 예시**: "코루틴 디스패처 차이점 테스트 만들어줘"

#### 2.3 Structured Concurrency
- Parent-child 관계
- Job hierarchy
- `coroutineScope` - 구조화된 동시성 블록
- `supervisorScope` - 자식 실패가 부모에 영향 없음
- 취소 전파 (Cancellation propagation)

**테스트 예시**: "구조화된 동시성 테스트 만들어줘"

#### 2.4 Coroutine Exception Handling
- `try-catch` in coroutines
- `CoroutineExceptionHandler`
- `SupervisorJob` vs `Job` 예외 처리 차이
- 취소 예외 (`CancellationException`)

**테스트 예시**: "코루틴 예외 처리 테스트 만들어줘"

#### 2.5 Coroutine Cancellation
- `Job.cancel()` - 코루틴 취소
- `isActive` - 활성 상태 확인
- `ensureActive()` - 취소 체크
- `NonCancellable` - 취소 불가능한 블록
- Cooperative cancellation (취소 협조)

**테스트 예시**: "코루틴 취소 메커니즘 테스트 만들어줘"

---

### Level 3: Flow 심화
Reactive stream 프로그래밍

#### 3.1 Flow Basics
- Cold stream vs Hot stream
- `flow { }` builder
- `emit()` - 값 방출
- `collect()` - 값 수집
- Flow는 suspend 함수에서만 수집 가능

**테스트 예시**: "Flow 기초 사용법 테스트 만들어줘"

#### 3.2 Flow Operators
- **변환**: `map`, `transform`, `flatMapConcat`, `flatMapMerge`
- **필터링**: `filter`, `take`, `drop`, `distinctUntilChanged`
- **결합**: `zip`, `combine`
- **시간**: `debounce`, `sample`, `delay`
- **예외 처리**: `catch`, `retry`, `retryWhen`

**테스트 예시**:
- "Flow map vs flatMap 테스트 만들어줘"
- "Flow debounce 사용법 테스트 만들어줘"

#### 3.3 StateFlow & SharedFlow
- **StateFlow**
  - Hot stream
  - 항상 최신 값 보유
  - 초기값 필수
  - Conflation (값 병합)
  - LiveData 대체

- **SharedFlow**
  - Hot stream
  - 여러 구독자에게 브로드캐스트
  - Replay cache 설정 가능
  - Event bus 패턴

**테스트 예시**:
- "StateFlow vs SharedFlow 비교 테스트 만들어줘"
- "StateFlow 구독자 여러 개 테스트 만들어줘"

#### 3.4 Flow Context & Dispatchers
- `flowOn()` - Flow의 실행 컨텍스트 변경
- Flow는 기본적으로 caller의 컨텍스트 사용
- `buffer()` - 백프레셔 제어
- `conflate()` - 가장 최신 값만 사용

**테스트 예시**: "Flow flowOn 동작 테스트 만들어줘"

#### 3.5 Advanced Flow Patterns
- `channelFlow` - Channel 기반 Flow
- `callbackFlow` - 콜백을 Flow로 변환
- `shareIn` - Flow를 SharedFlow로 변환
- `stateIn` - Flow를 StateFlow로 변환

**테스트 예시**:
- "callbackFlow 사용법 테스트 만들어줘"
- "shareIn vs stateIn 비교 테스트 만들어줘"

---

## 테스트 작성 패턴

### Pattern 1: Basic API Usage
단일 API의 기본 사용법 학습

```kotlin
class FlowBasicTest : FunSpec({
    test("flow는 cold stream이다") {
        // given
        var emitCount = 0
        val flow = flow {
            emitCount++
            emit(1)
        }

        // when: 여러 번 collect
        flow.collect()
        flow.collect()

        // then: 매번 새로 실행됨
        emitCount shouldBe 2
    }
})
```

### Pattern 2: Comparison Tests
두 가지 방법을 비교하여 차이점 학습

```kotlin
class LaunchVsAsyncTest : FunSpec({
    test("launch는 Job 반환") { /* ... */ }
    test("async는 Deferred 반환") { /* ... */ }
    test("launch는 결과 없음") { /* ... */ }
    test("async는 결과 반환") { /* ... */ }
})
```

### Pattern 3: Edge Cases
엣지 케이스와 주의사항 학습

```kotlin
class CoroutineCancellationTest : FunSpec({
    test("취소된 코루틴에서 suspend 함수 호출 시 CancellationException") {
        // given
        val job = CoroutineScope(Dispatchers.Default).launch {
            try {
                delay(1000)
            } catch (e: CancellationException) {
                // then: 취소 예외 발생
                throw e
            }
        }

        // when
        delay(50)
        job.cancel()

        shouldThrow<CancellationException> {
            job.join()
        }
    }
})
```

### Pattern 4: Real-World Scenarios
실제 사용 시나리오 기반 학습

```kotlin
class FlowPracticalTest : FunSpec({
    test("검색어 입력 시 debounce로 API 호출 횟수 줄이기") {
        // given: 사용자가 빠르게 입력하는 시나리오
        val searchQuery = MutableStateFlow("")
        val apiCallCount = atomic(0)

        val searchResults = searchQuery
            .debounce(300) // 300ms 대기
            .filter { it.isNotEmpty() }
            .map { query ->
                apiCallCount.incrementAndGet()
                "Results for $query"
            }

        // when: "안드로이드" 빠르게 입력
        searchQuery.value = "안"
        delay(100)
        searchQuery.value = "안드"
        delay(100)
        searchQuery.value = "안드로이드"
        delay(400) // debounce 대기

        // then: API는 최종 값만 호출됨
        searchResults.first() shouldBe "Results for 안드로이드"
        apiCallCount.value shouldBe 1
    }
})
```

---

## Kotest Matchers 치트시트

### 기본 검증
```kotlin
result shouldBe expected              // equals
result shouldNotBe unexpected         // not equals
result.shouldBeNull()                 // null check
result.shouldNotBeNull()              // non-null check
```

### 타입 검증
```kotlin
result.shouldBeInstanceOf<Type>()     // 타입 확인
result shouldBeInstanceOf Job::class  // 타입 확인 (alternative)
```

### 컬렉션 검증
```kotlin
list shouldHaveSize 3                 // 크기 확인
list shouldContain "item"             // 포함 확인
list shouldContainAll listOf("a", "b") // 여러 항목 포함
list.shouldBeEmpty()                  // 빈 컬렉션
list.shouldContainExactly("a", "b")   // 정확한 순서와 내용
```

### 숫자 검증
```kotlin
number shouldBeGreaterThan 10         // >
number shouldBeLessThan 100           // <
number shouldBeInRange 10..100        // 범위 내
```

### 예외 검증
```kotlin
shouldThrow<ExceptionType> {          // 예외 발생 확인
    // code that throws
}

shouldThrowAny {                      // 어떤 예외든 확인
    // code that throws
}
```

### Boolean 검증
```kotlin
result.shouldBeTrue()                 // true 확인
result.shouldBeFalse()                // false 확인
```

### 코루틴 검증
```kotlin
job.isActive shouldBe true            // Job 상태
job.isCancelled shouldBe false
flow.count() shouldBe 5               // Flow 아이템 개수
```

---

## 추천 학습 경로

### 초보자 경로 (2주)
**Week 1**: Kotlin 기초
1. Collections 기본 연산
2. Lambda & 고차함수
3. Extension functions
4. Data class & Sealed class

**Week 2**: Coroutines 기초
1. launch vs async
2. Dispatchers
3. 코루틴 취소
4. 예외 처리

### 중급자 경로 (2주)
**Week 1**: Coroutines 심화
1. Structured concurrency
2. SupervisorJob vs Job
3. Coroutine scope 관리
4. 테스트 작성 (kotlinx-coroutines-test)

**Week 2**: Flow 기초
1. Flow builder & collect
2. Flow operators (map, filter, transform)
3. StateFlow 기본 사용
4. SharedFlow 기본 사용

### 고급자 경로 (2주)
**Week 1**: Flow 심화
1. flowOn & dispatchers
2. Flow 예외 처리 (catch, retry)
3. Flow 결합 (zip, combine)
4. debounce, sample 등 시간 연산자

**Week 2**: Advanced Patterns
1. callbackFlow & channelFlow
2. shareIn & stateIn
3. Flow testing 패턴
4. 실전 아키텍처 패턴 (Repository, UseCase)

---

## 일반적인 실수와 해결법

### 실수 1: GlobalScope 사용
```kotlin
// ❌ Bad
GlobalScope.launch { /* ... */ }

// ✅ Good
class MyViewModel : ViewModel() {
    init {
        viewModelScope.launch { /* ... */ }
    }
}
```

**이유**: GlobalScope는 앱 전체 생명주기를 가지므로 메모리 누수 위험

### 실수 2: Flow를 suspend 아닌 곳에서 collect
```kotlin
// ❌ Bad
fun loadData() {
    flow.collect { /* ... */ } // Compile error!
}

// ✅ Good
suspend fun loadData() {
    flow.collect { /* ... */ }
}
```

**이유**: collect는 suspend 함수

### 실수 3: 취소 협조하지 않는 코루틴
```kotlin
// ❌ Bad
launch {
    while (true) {
        // CPU-intensive work
        doHeavyComputation()
    }
}

// ✅ Good
launch {
    while (isActive) {
        // CPU-intensive work
        doHeavyComputation()
    }
}
```

**이유**: isActive 체크로 취소에 협조해야 함

### 실수 4: StateFlow 초기값 없음
```kotlin
// ❌ Bad
val state: StateFlow<String> // Compile error!

// ✅ Good
val state = MutableStateFlow("")
```

**이유**: StateFlow는 항상 값을 가져야 함 (초기값 필수)

### 실수 5: Flow에서 exception 방출
```kotlin
// ❌ Bad
flow {
    emit(1)
    throw Exception("Error")
}

// ✅ Good
flow {
    emit(1)
    emit(2)
}.catch { e ->
    emit(0) // 에러 시 기본값
}
```

**이유**: catch 연산자로 예외 처리

---

## 유용한 참고 자료

### 공식 문서
- [Kotlin 공식 문서](https://kotlinlang.org/docs/home.html)
- [Coroutines 가이드](https://kotlinlang.org/docs/coroutines-guide.html)
- [Flow 가이드](https://kotlinlang.org/docs/flow.html)

### 학습 자료
- [Kotlin Koans](https://play.kotlinlang.org/koans) - 대화형 연습
- [Kotlin by Example](https://play.kotlinlang.org/byExample) - 예제 모음
- [Coroutines Codelab](https://developer.android.com/codelabs/kotlin-coroutines)

### 테스팅 가이드
- [Kotest 문서](https://kotest.io/)
- [kotlinx-coroutines-test](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)
