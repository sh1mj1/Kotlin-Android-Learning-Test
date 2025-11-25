# Learning Progress Reference Guide

## 전체 학습 커리큘럼

이 문서는 `LEARNING_PROGRESS.md`에 포함될 전체 학습 주제 목록과 의존성 관계를 정의합니다.

---

## 📚 Kotlin 기초 (12 topics)

### Level 1: 기초 (4 topics)
1. **Collections 기본 연산**
   - List, Set, Map 생성 및 접근
   - 난이도: ⭐ 기초
   - 선수 과목: 없음

2. **Lambda & 고차함수**
   - 람다 표현식, it vs 명시적 파라미터
   - 난이도: ⭐ 기초
   - 선수 과목: 없음

3. **Extension Functions**
   - 확장 함수 정의 및 사용
   - 난이도: ⭐ 기초
   - 선수 과목: 없음

4. **Data Classes & Sealed Classes**
   - data class, sealed class, when exhaustiveness
   - 난이도: ⭐ 기초
   - 선수 과목: 없음

### Level 2: 중급 (5 topics)
5. **Collections 고급 연산**
   - map, filter, fold, groupBy, partition
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Collections 기본 연산, Lambda

6. **Scope Functions**
   - let, run, with, apply, also
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Lambda

7. **Delegation Pattern**
   - by lazy, by observable, class delegation
   - 난이도: ⭐⭐ 중급
   - 선수 과목: 없음

8. **Sequence vs Collection**
   - Lazy evaluation, 성능 차이
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Collections 고급 연산

9. **Null Safety**
   - ?, !!, ?:, let, requireNotNull
   - 난이도: ⭐⭐ 중급
   - 선수 과목: 없음

### Level 3: 고급 (3 topics)
10. **Inline Functions**
    - inline, noinline, crossinline, reified
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: Lambda, 고차함수

11. **Generics**
    - Generic classes, functions, in/out variance
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: 없음

12. **DSL (Domain Specific Language)**
    - Builder pattern with lambdas
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: Lambda, Scope Functions

---

## ⚡ Coroutines (10 topics)

### Level 1: 기초 (4 topics)
1. **suspend 함수**
   - suspend 키워드, runBlocking
   - 난이도: ⭐ 기초
   - 선수 과목: 없음

2. **launch vs async**
   - Job, Deferred, 반환 값 차이
   - 난이도: ⭐ 기초
   - 선수 과목: suspend 함수

3. **Dispatchers**
   - Default, IO, Main, Unconfined
   - 난이도: ⭐⭐ 중급
   - 선수 과목: launch vs async

4. **CoroutineScope**
   - GlobalScope, viewModelScope, lifecycleScope
   - 난이도: ⭐⭐ 중급
   - 선수 과목: launch vs async

### Level 2: 중급 (4 topics)
5. **Structured Concurrency**
   - coroutineScope, supervisorScope, parent-child 관계
   - 난이도: ⭐⭐ 중급
   - 선수 과목: launch vs async, CoroutineScope

6. **Coroutine Cancellation**
   - cancel(), isActive, ensureActive(), cooperative cancellation
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Structured Concurrency

7. **Exception Handling**
   - try-catch, CoroutineExceptionHandler, SupervisorJob
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Structured Concurrency

8. **Coroutine Context**
   - CoroutineContext, Job, Dispatcher 조합
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Dispatchers, CoroutineScope

### Level 3: 고급 (2 topics)
9. **Custom Dispatchers**
   - newSingleThreadContext, asCoroutineDispatcher
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Dispatchers, Coroutine Context

10. **Coroutine Testing**
    - TestDispatcher, runTest, advanceUntilIdle
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: 모든 Coroutines 기초

---

## 🌊 Flow (8 topics)

### Level 1: 기초 (3 topics)
1. **Flow 기초**
   - flow builder, emit, collect, cold stream
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Coroutines - launch, suspend

2. **Flow Operators - 변환**
   - map, filter, transform
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Flow 기초

3. **Flow Operators - 결합**
   - zip, combine, flatMapConcat, flatMapMerge
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Flow Operators - 변환

### Level 2: 중급 (3 topics)
4. **StateFlow vs SharedFlow**
   - Hot stream, replay cache, conflation
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Flow 기초

5. **Flow Context & Dispatchers**
   - flowOn, buffer, conflate
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Coroutines - Dispatchers, Flow 기초

6. **Flow 예외 처리**
   - catch, retry, retryWhen
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Flow 기초, Coroutines - Exception Handling

### Level 3: 고급 (2 topics)
7. **callbackFlow & channelFlow**
   - Callback을 Flow로 변환, 양방향 Flow
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Flow 기초, Coroutines 중급

8. **shareIn & stateIn**
   - Cold → Hot 변환, scope, replay, started
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: StateFlow vs SharedFlow

---

## 🎨 Compose (12 topics)

### Level 1: 기초 (4 topics)
1. **Composable Functions**
   - @Composable, recomposition, composition
   - 난이도: ⭐ 기초
   - 선수 과목: Kotlin 기초

2. **State & remember**
   - mutableStateOf, remember, rememberSaveable
   - 난이도: ⭐ 기초
   - 선수 과목: Composable Functions

3. **Modifier 기초**
   - padding, size, background, clickable, 순서
   - 난이도: ⭐ 기초
   - 선수 과목: Composable Functions

4. **Layout - Row, Column, Box**
   - 기본 레이아웃, arrangement, alignment
   - 난이도: ⭐ 기초
   - 선수 과목: Modifier 기초

### Level 2: 중급 (5 topics)
5. **Recomposition 최적화**
   - Smart recomposition, derivedStateOf, remember + key
   - 난이도: ⭐⭐ 중급
   - 선수 과목: State & remember

6. **LaunchedEffect**
   - Side effect, key 기반 재실행, coroutine scope
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Coroutines - launch, Composable Functions

7. **DisposableEffect & SideEffect**
   - Cleanup, onDispose, 실행 시점
   - 난이도: ⭐⭐ 중급
   - 선수 과목: LaunchedEffect

8. **State Hoisting**
   - Stateless composable, 단방향 데이터 플로우
   - 난이도: ⭐⭐ 중급
   - 선수 과목: State & remember

9. **rememberCoroutineScope & rememberUpdatedState**
   - Event handler에서 코루틴, 최신 값 캡처 방지
   - 난이도: ⭐⭐ 중급
   - 선수 과목: LaunchedEffect, Coroutines

### Level 3: 고급 (3 topics)
10. **Slot API Pattern**
    - Content lambda, 재사용 가능한 컴포넌트
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: State Hoisting

11. **Custom Layout**
    - Layout composable, Measurable, Placeable
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: Layout - Row, Column, Box

12. **Compose Testing**
    - ComposeTestRule, semantics, UI 상호작용
    - 난이도: ⭐⭐⭐ 고급
    - 선수 과목: 모든 Compose 기초

---

## 📱 Android (8 topics)

### Level 1: 기초 (3 topics)
1. **Activity Lifecycle**
   - onCreate, onStart, onResume, onPause, onStop, onDestroy
   - 난이도: ⭐ 기초
   - 선수 과목: Kotlin 기초

2. **Fragment Lifecycle**
   - Fragment 생명주기, FragmentManager, Transaction
   - 난이도: ⭐ 기초
   - 선수 과목: Activity Lifecycle

3. **Intent & Data Passing**
   - Explicit/Implicit Intent, extras, Parcelable
   - 난이도: ⭐ 기초
   - 선수 과목: Activity Lifecycle

### Level 2: 중급 (3 topics)
4. **ViewModel**
   - ViewModel 생명주기, ViewModelProvider, onCleared
   - 난이도: ⭐⭐ 중급
   - 선수 과목: Activity Lifecycle

5. **LiveData**
   - observe, Transformations, MediatorLiveData
   - 난이도: ⭐⭐ 중급
   - 선수 과목: ViewModel

6. **SavedStateHandle**
   - Process death 대응, state 복원
   - 난이도: ⭐⭐ 중급
   - 선수 과목: ViewModel

### Level 3: 고급 (2 topics)
7. **Navigation Component**
   - NavController, NavGraph, SafeArgs, Deep links
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Fragment Lifecycle

8. **LifecycleObserver**
   - Lifecycle-aware components, DefaultLifecycleObserver
   - 난이도: ⭐⭐⭐ 고급
   - 선수 과목: Activity Lifecycle, Fragment Lifecycle

---

## 📊 진도 추적 템플릿

### 초기 LEARNING_PROGRESS.md 템플릿

```markdown
# Kotlin & Android Learning Progress

**생성일**: 2025-01-24
**최근 업데이트**: 2025-01-24

---

## 📊 Overall Statistics

| Category | Completed | Total | Progress |
|----------|-----------|-------|----------|
| Kotlin 기초 | 0 | 12 | 0% |
| Coroutines | 0 | 10 | 0% |
| Flow | 0 | 8 | 0% |
| Compose | 0 | 12 | 0% |
| Android | 0 | 8 | 0% |
| **Total** | **0** | **50** | **0%** |

---

## 📚 Detailed Progress

### 🔷 Kotlin 기초 (0 / 12)

#### Level 1: 기초 (0 / 4)
- [ ] Collections 기본 연산
- [ ] Lambda & 고차함수
- [ ] Extension Functions
- [ ] Data Classes & Sealed Classes

#### Level 2: 중급 (0 / 5)
- [ ] Collections 고급 연산
- [ ] Scope Functions
- [ ] Delegation Pattern
- [ ] Sequence vs Collection
- [ ] Null Safety

#### Level 3: 고급 (0 / 3)
- [ ] Inline Functions
- [ ] Generics
- [ ] DSL

---

### ⚡ Coroutines (0 / 10)

#### Level 1: 기초 (0 / 4)
- [ ] suspend 함수
- [ ] launch vs async
- [ ] Dispatchers
- [ ] CoroutineScope

#### Level 2: 중급 (0 / 4)
- [ ] Structured Concurrency
- [ ] Coroutine Cancellation
- [ ] Exception Handling
- [ ] Coroutine Context

#### Level 3: 고급 (0 / 2)
- [ ] Custom Dispatchers
- [ ] Coroutine Testing

---

### 🌊 Flow (0 / 8)

#### Level 1: 기초 (0 / 3)
- [ ] Flow 기초
- [ ] Flow Operators - 변환
- [ ] Flow Operators - 결합

#### Level 2: 중급 (0 / 3)
- [ ] StateFlow vs SharedFlow
- [ ] Flow Context & Dispatchers
- [ ] Flow 예외 처리

#### Level 3: 고급 (0 / 2)
- [ ] callbackFlow & channelFlow
- [ ] shareIn & stateIn

---

### 🎨 Compose (0 / 12)

#### Level 1: 기초 (0 / 4)
- [ ] Composable Functions
- [ ] State & remember
- [ ] Modifier 기초
- [ ] Layout - Row, Column, Box

#### Level 2: 중급 (0 / 5)
- [ ] Recomposition 최적화
- [ ] LaunchedEffect
- [ ] DisposableEffect & SideEffect
- [ ] State Hoisting
- [ ] rememberCoroutineScope & rememberUpdatedState

#### Level 3: 고급 (0 / 3)
- [ ] Slot API Pattern
- [ ] Custom Layout
- [ ] Compose Testing

---

### 📱 Android (0 / 8)

#### Level 1: 기초 (0 / 3)
- [ ] Activity Lifecycle
- [ ] Fragment Lifecycle
- [ ] Intent & Data Passing

#### Level 2: 중급 (0 / 3)
- [ ] ViewModel
- [ ] LiveData
- [ ] SavedStateHandle

#### Level 3: 고급 (0 / 2)
- [ ] Navigation Component
- [ ] LifecycleObserver

---

## 🎯 Recommended Learning Path

### Phase 1: Kotlin Foundation (1-2 weeks)
순서대로 학습 추천:
1. Collections 기본 연산
2. Lambda & 고차함수
3. Extension Functions
4. Data Classes & Sealed Classes

### Phase 2: Coroutines Basics (1 week)
순서대로 학습 추천:
1. suspend 함수
2. launch vs async
3. Dispatchers
4. CoroutineScope

### Phase 3: Flow Basics (1 week)
선수 과목: Coroutines 기초 완료
1. Flow 기초
2. Flow Operators - 변환
3. StateFlow vs SharedFlow

### Phase 4: Compose Basics (1-2 weeks)
선수 과목: Kotlin 기초 완료
1. Composable Functions
2. State & remember
3. Modifier 기초
4. Layout - Row, Column, Box

### Phase 5: Android Basics (1 week)
선수 과목: Kotlin 기초 완료
1. Activity Lifecycle
2. Fragment Lifecycle
3. ViewModel

### Phase 6: Integration (2-3 weeks)
모든 기초 완료 후:
- Compose + Flow (UI + reactive data)
- ViewModel + Flow (architecture)
- Navigation + Compose (routing)

---

## 📅 Recent Activity

No activity yet. Start learning!

---

## 💡 Tips

1. **Follow the prerequisites**: 선수 과목을 먼저 완료하세요
2. **Practice with tests**: 각 주제마다 학습 테스트를 작성하세요
3. **Don't rush**: 이해하고 넘어가는 것이 중요합니다
4. **Review regularly**: 정기적으로 복습하세요
5. **Build projects**: 배운 내용을 실제 프로젝트에 적용하세요

---

## 🔗 Quick Links

- [Kotlin 공식 문서](https://kotlinlang.org/docs/home.html)
- [Coroutines 가이드](https://kotlinlang.org/docs/coroutines-guide.html)
- [Compose 문서](https://developer.android.com/jetpack/compose)
- [Android Developer](https://developer.android.com)
```

---

## 진도 업데이트 규칙

### 체크박스 형식
```markdown
- [x] Topic Name ✅ 2025-01-24
```

### 통계 자동 계산
- 카테고리별: 완료 개수 / 전체 개수
- 전체: 모든 완료 / 모든 항목
- 퍼센트: (완료 / 전체) * 100

### Recent Activity 업데이트
최근 5개 활동 표시:
```markdown
## 📅 Recent Activity

- 2025-01-24: ✅ Coroutines - launch vs async
- 2025-01-23: ✅ Kotlin - Lambda & 고차함수
- 2025-01-22: ✅ Kotlin - Collections 기본 연산
```

---

## 추천 알고리즘

### 규칙 1: Prerequisites First
선수 과목이 완료되지 않은 주제는 낮은 우선순위

### 규칙 2: Continue Current Path
같은 카테고리 내 다음 Level을 우선 추천

### 규칙 3: Diversify
한 카테고리만 집중하지 말고 다양하게 학습 권장

### 규칙 4: Difficulty Progression
기초 → 중급 → 고급 순서 유지

### 예시
**현재 상태**:
- Kotlin: Collections 기본, Lambda 완료
- Coroutines: 없음
- Flow: 없음

**추천 순서**:
1. Kotlin - Extension Functions (같은 카테고리, 기초)
2. Kotlin - Data Classes & Sealed Classes (같은 카테고리, 기초)
3. Coroutines - suspend 함수 (새 카테고리, 기초)

---

이 참조 문서를 기반으로 `LEARNING_PROGRESS.md`가 생성되고 관리됩니다.
