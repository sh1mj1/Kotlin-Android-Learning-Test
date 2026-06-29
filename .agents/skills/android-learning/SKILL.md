---
name: android-learning
description: "Creates Android framework learning tests for core components (Context, Activity, Fragment, Intent), architecture (ViewModel, LiveData, SavedStateHandle), navigation (NavController, SafeArgs), and lifecycle (Lifecycle, LifecycleOwner). Generates complete instrumentation tests with JUnit4 and AndroidJUnit4Runner in src/androidTest/java/learning/android/"
---

# Android Learning Test Generator

## Overview
This skill creates interactive learning tests for Android framework components and architecture patterns. Tests are generated as complete, executable instrumentation tests using JUnit4 and AndroidJUnit4Runner that run on Android devices or emulators.

## Scope
This skill handles **Android framework** topics:
- **Android Basics**: Context, Activity, Fragment, Intent, Resources
- **Architecture Components**: ViewModel, LiveData, SavedStateHandle, ViewModelProvider
- **Navigation**: NavController, NavGraph, SafeArgs, Deep links
- **Lifecycle**: Lifecycle, LifecycleOwner, LifecycleObserver, ProcessLifecycleOwner
- **Testing**: AndroidJUnit4, Espresso, ActivityScenario, FragmentScenario

## Test Generation Strategy

### 1. Analyze Request
When the user requests an Android learning test, identify:
- **Topic**: Which Android component/API to learn
- **Depth**: Basic usage, lifecycle events, or integration patterns
- **Test Type**: Unit test (Robolectric) or Instrumentation test (real device)

### 2. Create Test File
Generate test file in: `src/androidTest/java/learning/android/[Topic]Test.kt`

**File naming convention:**
- Single concept: `ViewModelLifecycleTest.kt`
- Comparison: `LiveDataVsFlowTest.kt`
- Category: `AndroidNavigationTest.kt`

### 3. Test Structure
Use JUnit4 with AndroidJUnit4Runner:

```kotlin
@RunWith(AndroidJUnit4::class)
class [Topic]Test {
    @Test
    fun test_description() {
        // given: 테스트 설정

        // when: 테스트할 동작 실행

        // then: 결과 검증 (AssertJ)
        assertThat(result).isEqualTo(expected)
    }
}
```

### 4. Learning Comments
Add educational comments explaining:
- **Why**: Why this Android pattern exists
- **When**: When to use this component
- **Gotchas**: Common Android mistakes to avoid

## Output Format

### Complete Test File
Generate a fully functional test file with:
1. **Package declaration**: `package learning.android`
2. **Imports**: JUnit4, AndroidJUnit4, AssertJ, Android components
3. **Test class**: Annotated with `@RunWith(AndroidJUnit4::class)`
4. **Setup/Teardown**: `@Before`, `@After` for lifecycle management
5. **Multiple test cases**: 3-7 test cases covering different aspects
6. **Learning comments**: Korean comments explaining key concepts
7. **Execution instructions**: How to run instrumentation tests

### Example Output
For request: "ViewModel lifecycle 테스트 만들어줘"

**Generated file**: `src/androidTest/java/learning/android/ViewModelLifecycleTest.kt`

```kotlin
package learning.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 학습 목표: ViewModel의 생명주기 이해
 * - ViewModel은 configuration change에도 살아남는다
 * - onCleared()는 Activity/Fragment가 완전히 종료될 때만 호출
 * - ViewModelStore가 ViewModel 인스턴스를 관리
 */
@RunWith(AndroidJUnit4::class)
class ViewModelLifecycleTest {

    private lateinit var viewModelStore: ViewModelStore
    private lateinit var factory: ViewModelProvider.Factory

    @Before
    fun setup() {
        viewModelStore = ViewModelStore()
        factory = ViewModelProvider.NewInstanceFactory()
    }

    @Test
    fun viewModel_survives_configuration_change() {
        // given: ViewModel 생성
        val provider1 = ViewModelProvider(viewModelStore, factory)
        val viewModel1 = provider1[TestViewModel::class.java]
        viewModel1.data = "Test Data"

        // when: configuration change 시뮬레이션 (같은 ViewModelStore 재사용)
        val provider2 = ViewModelProvider(viewModelStore, factory)
        val viewModel2 = provider2[TestViewModel::class.java]

        // then: 같은 ViewModel 인스턴스 반환됨
        assertThat(viewModel1).isSameAs(viewModel2)
        assertThat(viewModel2.data).isEqualTo("Test Data")
    }

    @Test
    fun viewModel_onCleared_called_when_store_cleared() {
        // given: ViewModel 생성
        val provider = ViewModelProvider(viewModelStore, factory)
        val viewModel = provider[TestViewModel::class.java]

        // when: ViewModelStore clear (Activity 완전 종료 시)
        viewModelStore.clear()

        // then: onCleared() 호출됨
        assertThat(viewModel.isCleared).isTrue()
    }

    @Test
    fun multiple_viewModels_can_coexist_in_same_store() {
        // given: 여러 ViewModel 생성
        val provider = ViewModelProvider(viewModelStore, factory)
        val viewModel1 = provider[TestViewModel::class.java]
        val viewModel2 = provider[TestViewModel2::class.java]

        // when: 각각 데이터 설정
        viewModel1.data = "ViewModel 1"
        viewModel2.data = "ViewModel 2"

        // then: 독립적으로 관리됨
        assertThat(viewModel1.data).isEqualTo("ViewModel 1")
        assertThat(viewModel2.data).isEqualTo("ViewModel 2")
    }

    @Test
    fun same_viewModel_class_returns_same_instance() {
        // given: 같은 클래스로 여러 번 요청
        val provider = ViewModelProvider(viewModelStore, factory)
        val viewModel1 = provider[TestViewModel::class.java]
        val viewModel2 = provider[TestViewModel::class.java]

        // then: 항상 같은 인스턴스 반환
        assertThat(viewModel1).isSameAs(viewModel2)
    }

    // 테스트용 ViewModel
    class TestViewModel : ViewModel() {
        var data: String = ""
        var isCleared: Boolean = false

        override fun onCleared() {
            super.onCleared()
            isCleared = true
        }
    }

    class TestViewModel2 : ViewModel() {
        var data: String = ""
    }
}

/**
 * 실행 방법:
 * 1. 터미널: ./gradlew connectedAndroidTest
 * 2. IDE: 클래스 우클릭 → Run 'ViewModelLifecycleTest'
 * 3. 에뮬레이터 또는 실제 디바이스 필요
 *
 * 학습 포인트:
 * - ViewModel은 ViewModelStore에 저장되어 configuration change에 살아남음
 * - onCleared()는 Activity finish() 또는 Fragment remove 시에만 호출
 * - 같은 ViewModelStore를 사용하면 같은 ViewModel 인스턴스를 얻음
 * - 각 ViewModel 클래스는 독립적으로 관리됨 (클래스명이 key)
 */
```

## Usage Instructions

### When This Skill Activates
The skill automatically activates when you request:
- "ViewModel 테스트 만들어줘"
- "Lifecycle 학습 테스트"
- "Navigation 사용법 테스트"
- "Context vs ApplicationContext 차이"
- Any Android framework component learning request

### What You Provide
1. **Topic**: What to learn (e.g., "ViewModel", "Lifecycle", "Navigation")
2. **Optional depth**: "기초", "심화", "비교" etc.

### What You Get
1. **Complete test file** in correct location (`src/androidTest/`)
2. **Runnable instrumentation tests** with JUnit4 + AndroidJUnit4
3. **Learning comments** in Korean
4. **Execution instructions** (requires emulator/device)
5. **Key takeaways** summary

## Dependencies Required
Ensure your `build.gradle.kts` includes:

```kotlin
dependencies {
    // Android core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")

    // Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("org.assertj:assertj-core:3.24.2")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
```

## Best Practices

### 1. Use ActivityScenario for Activity Tests
```kotlin
@Test
fun activity_lifecycle_test() {
    ActivityScenario.launch(MyActivity::class.java).use { scenario ->
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onActivity { activity ->
            assertThat(activity.lifecycle.currentState)
                .isEqualTo(Lifecycle.State.RESUMED)
        }
    }
}
```

### 2. Use FragmentScenario for Fragment Tests
```kotlin
@Test
fun fragment_lifecycle_test() {
    FragmentScenario.launchInContainer(MyFragment::class.java).use { scenario ->
        scenario.onFragment { fragment ->
            assertThat(fragment.isAdded).isTrue()
        }
    }
}
```

### 3. Test Lifecycle Events
```kotlin
@Test
fun lifecycle_observer_receives_events() {
    val lifecycle = LifecycleRegistry(mockLifecycleOwner)
    val observer = TestLifecycleObserver()

    lifecycle.addObserver(observer)
    lifecycle.currentState = Lifecycle.State.CREATED

    assertThat(observer.onCreateCalled).isTrue()
}
```

### 4. Test ViewModel with SavedStateHandle
```kotlin
@Test
fun viewModel_restores_state_from_savedStateHandle() {
    val savedState = SavedStateHandle(mapOf("key" to "value"))
    val viewModel = MyViewModel(savedState)

    assertThat(viewModel.restoredData).isEqualTo("value")
}
```

### 5. Use AssertJ for Assertions
```kotlin
// ✅ Good: AssertJ (readable, fluent)
assertThat(result).isEqualTo(expected)
assertThat(list).hasSize(3)
assertThat(value).isNotNull()

// ❌ Avoid: JUnit assertions (less readable)
assertEquals(expected, result)
```

## Troubleshooting

### If androidTest Directory Doesn't Exist
Create the directory structure:
```bash
mkdir -p app/src/androidTest/java/learning/android
```

### If Instrumentation Runner Not Configured
Add to `app/build.gradle.kts`:
```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
```

### If Tests Don't Run
1. Ensure emulator/device is connected: `adb devices`
2. Check test runner is configured
3. Sync Gradle files

### If AssertJ Not Available
Add dependency:
```kotlin
androidTestImplementation("org.assertj:assertj-core:3.24.2")
```

## Related Skills
- **kotlin-learning**: For pure Kotlin/coroutines tests (no Android)
- **compose-learning**: For Jetpack Compose UI tests
- **learning-progress**: To track completed Android learning topics
