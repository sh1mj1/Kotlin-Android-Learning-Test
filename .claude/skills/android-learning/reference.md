# Android Learning Reference Guide

## 학습 커리큘럼

### Level 1: Android 기초
핵심 Android 컴포넌트

#### 1.1 Context
- Application Context vs Activity Context
- Context 사용 시점
- Context 메모리 누수 주의사항
- ContextWrapper와 ContextThemeWrapper

**테스트 예시**: "Context vs ApplicationContext 차이 테스트"

#### 1.2 Activity
- Activity 생명주기 (onCreate, onStart, onResume, onPause, onStop, onDestroy)
- Configuration change (화면 회전)
- savedInstanceState로 상태 복원
- Intent로 데이터 전달
- Activity Result API

**테스트 예시**:
- "Activity 생명주기 테스트"
- "화면 회전 시 데이터 보존 테스트"

#### 1.3 Fragment
- Fragment 생명주기
- Fragment와 Activity 생명주기 관계
- FragmentManager와 FragmentTransaction
- Fragment간 통신 (shared ViewModel)
- Fragment Result API

**테스트 예시**: "Fragment 생명주기 테스트"

#### 1.4 Intent
- Explicit Intent vs Implicit Intent
- Intent extras로 데이터 전달
- Intent filter
- PendingIntent

**테스트 예시**: "Intent로 데이터 전달 테스트"

#### 1.5 Resources
- getString(), getColor(), getDimension()
- Configuration qualifiers (언어, 화면 크기)
- Resources.Theme
- Typed arrays

**테스트 예시**: "Resources 접근 테스트"

---

### Level 2: Architecture Components
MVVM 아키텍처의 핵심

#### 2.1 ViewModel
- ViewModel 생명주기 (configuration change에 살아남음)
- ViewModelProvider와 Factory
- onCleared() cleanup
- SharedViewModel (Fragment 간 공유)
- ViewModelScope

**테스트 예시**:
- "ViewModel 생명주기 테스트"
- "SharedViewModel로 Fragment 통신 테스트"

#### 2.2 LiveData
- LiveData vs MutableLiveData
- observe() vs observeForever()
- Transformations.map, switchMap
- MediatorLiveData
- LiveData는 lifecycle-aware

**테스트 예시**:
- "LiveData 옵저빙 테스트"
- "Transformations.map 사용 테스트"

#### 2.3 SavedStateHandle
- ViewModel에서 상태 복원
- Process death 대응
- getLiveData() vs get()
- set() vs setSavedStateProvider()

**테스트 예시**: "SavedStateHandle로 상태 복원 테스트"

#### 2.4 ViewModelProvider
- Factory 패턴
- Custom Factory 구현
- AbstractSavedStateViewModelFactory
- ViewModelStoreOwner

**테스트 예시**: "Custom ViewModel Factory 테스트"

---

### Level 3: Navigation
화면 이동과 딥링크

#### 3.1 NavController
- navigate() 화면 이동
- popBackStack() 뒤로 가기
- currentBackStackEntry
- NavigationOptions (애니메이션, popUpTo)

**테스트 예시**: "NavController 화면 이동 테스트"

#### 3.2 NavGraph
- Navigation graph XML
- Nested graphs
- Start destination
- Global actions

**테스트 예시**: "NavGraph 탐색 테스트"

#### 3.3 SafeArgs
- 타입 안전한 argument 전달
- Generated classes (Directions, Args)
- Nullable vs non-nullable arguments
- Default values

**테스트 예시**: "SafeArgs로 데이터 전달 테스트"

#### 3.4 Deep Links
- Explicit deep link
- Implicit deep link
- PendingIntent로 deep link 실행
- Handle deep link in Activity

**테스트 예시**: "Deep link 동작 테스트"

---

### Level 4: Lifecycle
생명주기 관리

#### 4.1 Lifecycle
- Lifecycle.State (INITIALIZED, CREATED, STARTED, RESUMED, DESTROYED)
- Lifecycle.Event (ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY)
- currentState vs currentStateFlow

**테스트 예시**: "Lifecycle State 전환 테스트"

#### 4.2 LifecycleObserver
- DefaultLifecycleObserver 인터페이스
- @OnLifecycleEvent (deprecated)
- addObserver() / removeObserver()
- Custom lifecycle-aware components

**테스트 예시**: "LifecycleObserver 이벤트 수신 테스트"

#### 4.3 LifecycleOwner
- Activity와 Fragment는 LifecycleOwner
- Custom LifecycleOwner 구현
- LifecycleRegistry 사용

**테스트 예시**: "Custom LifecycleOwner 구현 테스트"

#### 4.4 ProcessLifecycleOwner
- 앱 전체 생명주기 관찰
- 포그라운드/백그라운드 전환 감지
- 앱 종료 감지 불가 (process kill은 감지 못함)

**테스트 예시**: "ProcessLifecycleOwner 포그라운드 감지 테스트"

---

## Android 테스트 작성 패턴

### Pattern 1: Activity Lifecycle Test
ActivityScenario로 생명주기 제어

```kotlin
@RunWith(AndroidJUnit4::class)
class ActivityLifecycleTest {

    @Test
    fun activity_goes_through_full_lifecycle() {
        // given
        val events = mutableListOf<Lifecycle.Event>()

        ActivityScenario.launch(TestActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onCreate(owner: LifecycleOwner) {
                        events.add(Lifecycle.Event.ON_CREATE)
                    }
                    override fun onStart(owner: LifecycleOwner) {
                        events.add(Lifecycle.Event.ON_START)
                    }
                    override fun onResume(owner: LifecycleOwner) {
                        events.add(Lifecycle.Event.ON_RESUME)
                    }
                })
            }

            // when: 생명주기 변경
            scenario.moveToState(Lifecycle.State.CREATED)
            scenario.moveToState(Lifecycle.State.RESUMED)

            // then: 이벤트 순서 검증
            assertThat(events).containsExactly(
                Lifecycle.Event.ON_CREATE,
                Lifecycle.Event.ON_START,
                Lifecycle.Event.ON_RESUME
            )
        }
    }
}
```

### Pattern 2: ViewModel Scoping Test
ViewModel이 올바른 scope에 생성되는지 검증

```kotlin
@RunWith(AndroidJUnit4::class)
class ViewModelScopingTest {

    @Test
    fun activity_scoped_viewModel_shared_across_fragments() {
        ActivityScenario.launch(TestActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // given: Activity scope ViewModel
                val activityViewModel = ViewModelProvider(activity)[SharedViewModel::class.java]
                activityViewModel.data.value = "Shared Data"

                // when: Fragment에서 같은 ViewModel 요청
                val fragment = TestFragment()
                activity.supportFragmentManager.beginTransaction()
                    .add(fragment, "test")
                    .commitNow()

                val fragmentViewModel = ViewModelProvider(activity)[SharedViewModel::class.java]

                // then: 같은 인스턴스
                assertThat(fragmentViewModel).isSameAs(activityViewModel)
                assertThat(fragmentViewModel.data.value).isEqualTo("Shared Data")
            }
        }
    }
}
```

### Pattern 3: Navigation Test
NavController를 사용한 화면 이동 검증

```kotlin
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Test
    fun navigate_to_detail_screen_with_arguments() {
        // given
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)

        FragmentScenario.launchInContainer(HomeFragment::class.java).use { scenario ->
            scenario.onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
            }

            // when: 상세 화면으로 이동
            val itemId = 123
            navController.navigate(
                HomeFragmentDirections.actionHomeToDetail(itemId)
            )

            // then: 목적지와 argument 검증
            assertThat(navController.currentDestination?.id).isEqualTo(R.id.detailFragment)
            assertThat(navController.currentBackStackEntry?.arguments?.getInt("itemId"))
                .isEqualTo(itemId)
        }
    }
}
```

### Pattern 4: LiveData Observation Test
LiveData 값 변경 감지 및 검증

```kotlin
@RunWith(AndroidJUnit4::class)
class LiveDataObservationTest {

    @Test
    fun liveData_emits_values_to_observer() {
        // given
        val lifecycle = LifecycleRegistry(mockLifecycleOwner)
        lifecycle.currentState = Lifecycle.State.RESUMED

        val liveData = MutableLiveData<String>()
        val observedValues = mutableListOf<String>()

        // when: Observer 등록
        liveData.observe(mockLifecycleOwner) { value ->
            observedValues.add(value)
        }

        // when: 값 변경
        liveData.value = "First"
        liveData.value = "Second"

        // then: 모든 값 수신
        assertThat(observedValues).containsExactly("First", "Second")
    }
}
```

---

## AndroidJUnit4 테스트 도구

### ActivityScenario
Activity 생명주기 제어

```kotlin
// Activity 시작
ActivityScenario.launch(MyActivity::class.java).use { scenario ->
    // Activity 접근
    scenario.onActivity { activity ->
        // activity 조작
    }

    // 생명주기 변경
    scenario.moveToState(Lifecycle.State.CREATED)
    scenario.moveToState(Lifecycle.State.RESUMED)

    // Configuration change 시뮬레이션
    scenario.recreate()
}
```

### FragmentScenario
Fragment 독립 테스트

```kotlin
// Fragment를 container에 launch
FragmentScenario.launchInContainer(MyFragment::class.java).use { scenario ->
    // Fragment 접근
    scenario.onFragment { fragment ->
        // fragment 조작
    }

    // 생명주기 변경
    scenario.moveToState(Lifecycle.State.STARTED)
}

// Arguments와 함께 launch
val args = bundleOf("key" to "value")
FragmentScenario.launchInContainer(MyFragment::class.java, args)
```

### Espresso 기본 사용법
UI 상호작용 및 검증

```kotlin
// View 찾기 및 조작
onView(withId(R.id.button)).perform(click())
onView(withId(R.id.editText)).perform(typeText("Hello"))
onView(withId(R.id.textView)).check(matches(withText("Expected")))

// RecyclerView 조작
onView(withId(R.id.recyclerView))
    .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
```

### ApplicationProvider
테스트용 Context 얻기

```kotlin
val context = ApplicationProvider.getApplicationContext<Context>()
val appContext = ApplicationProvider.getApplicationContext<Application>()
```

---

## AssertJ Android 확장

### Intent Assertions
```kotlin
val intent = Intent(context, MyActivity::class.java)
    .putExtra("key", "value")

assertThat(intent).hasAction(Intent.ACTION_VIEW)
assertThat(intent).hasExtra("key", "value")
assertThat(intent).hasComponent(ComponentName(context, MyActivity::class.java))
```

### Bundle Assertions
```kotlin
val bundle = bundleOf("key" to "value")

assertThat(bundle).containsKey("key")
assertThat(bundle.getString("key")).isEqualTo("value")
```

---

## 일반적인 실수와 해결법

### 실수 1: Activity Context 메모리 누수
```kotlin
// ❌ Bad: Activity context를 장기 참조
class MyManager(private val context: Context) {
    // Activity가 destroy되어도 context 참조 유지 → 메모리 누수
}

// ✅ Good: Application context 사용
class MyManager(private val context: Context) {
    private val appContext = context.applicationContext
}
```

### 실수 2: LiveData observeForever cleanup 누락
```kotlin
// ❌ Bad: observeForever 사용 후 removeObserver 안 함
liveData.observeForever { value ->
    // 메모리 누수!
}

// ✅ Good: removeObserver로 cleanup
val observer = Observer<String> { value -> }
liveData.observeForever(observer)
// ... later
liveData.removeObserver(observer)

// ✅ Better: lifecycle-aware observe 사용
liveData.observe(viewLifecycleOwner) { value ->
    // 자동으로 cleanup됨
}
```

### 실수 3: Fragment에서 Activity context 사용
```kotlin
// ❌ Bad: Fragment에서 activity context 사용
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val context = requireActivity() // Activity destroy 시 문제
}

// ✅ Good: requireContext() 사용
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val context = requireContext() // Fragment context
}
```

### 실수 4: Configuration change 시 ViewModel 재생성
```kotlin
// ❌ Bad: Activity/Fragment에서 직접 ViewModel 인스턴스 생성
class MyActivity : AppCompatActivity() {
    private val viewModel = MyViewModel() // 화면 회전 시 재생성됨!
}

// ✅ Good: ViewModelProvider 사용
class MyActivity : AppCompatActivity() {
    private val viewModel: MyViewModel by viewModels()
}
```

### 실수 5: savedInstanceState 잘못된 시점에 사용
```kotlin
// ❌ Bad: onSaveInstanceState에서 비동기 작업 결과 저장 시도
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel.fetchData() // 비동기 작업 결과를 어떻게 저장?
}

// ✅ Good: ViewModel + SavedStateHandle 사용
class MyViewModel(private val savedState: SavedStateHandle) : ViewModel() {
    init {
        viewModelScope.launch {
            val data = fetchData()
            savedState["data"] = data // 자동으로 저장됨
        }
    }
}
```

---

## 추천 학습 경로

### 초보자 경로 (2주)
**Week 1**: Android 기본 컴포넌트
1. Activity 생명주기
2. Fragment 생명주기
3. Intent로 데이터 전달
4. Context 사용법

**Week 2**: Architecture Components 기초
1. ViewModel 기본
2. LiveData 사용
3. ViewModelProvider
4. Configuration change 대응

### 중급자 경로 (2주)
**Week 1**: Architecture 심화
1. SavedStateHandle
2. SharedViewModel
3. Transformations (map, switchMap)
4. MediatorLiveData

**Week 2**: Navigation
1. NavController 기본
2. SafeArgs
3. Navigation graph
4. Deep links

### 고급자 경로 (2주)
**Week 1**: Lifecycle 심화
1. LifecycleObserver 구현
2. Custom lifecycle-aware components
3. ProcessLifecycleOwner
4. Lifecycle-aware coroutines

**Week 2**: Testing
1. ActivityScenario
2. FragmentScenario
3. Espresso UI testing
4. Navigation testing

---

## 유용한 참고 자료

### 공식 문서
- [Android Developer Guide](https://developer.android.com/guide)
- [Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Navigation Component](https://developer.android.com/guide/navigation)
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)

### 학습 자료
- [Android Basics in Kotlin](https://developer.android.com/courses/android-basics-kotlin/course)
- [Modern Android Development](https://developer.android.com/series/mad-skills)
- [Android Architecture Samples](https://github.com/android/architecture-samples)

### 테스팅 가이드
- [Testing Apps on Android](https://developer.android.com/training/testing)
- [Espresso Documentation](https://developer.android.com/training/testing/espresso)
- [Testing Codelabs](https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-basics)
