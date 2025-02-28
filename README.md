# Kotlin-Android-Learning-Test

<details>
  <summary><span style="font-size: 2.0em; font-weight: bold;">📌 Test Frameworks</span></summary>
Test Frameworks 는 Kotlin 을 사용하여 코틀린/안드로이드 앱을 테스트하는 방법을 학습한다.
    <details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 Kotest </span></summary>

Kotest 는 테스트가 TestContext -> Unit 함수로 정의된다.

이 함수가 테스트 로직을 포함한다.

이 함수에서 발생하는 모든 assert 문은 예외를 던지고, 프레임워크가 이를 가로채서 테스트가 실패했는지 성공했는지를 표시한다.

테스트 함수는 수동으로 정의되지 않는다.
대신에, Kotest DSL 은 이러한 함수가 생성되고 중첩될 수 있는 여러 가지 방법을 제공한다.
DSL 은 특정 테스팅 스타일을 구현하는 클래스를 상속하는 클래스를 생성하여 액세스된다.

### Test Style

* [KotestFunSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestFunSpec.kt)
* [KotestDescribeSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestDescribeSpec.kt)
* [KotestStringSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestStringSpec.kt)
* [KotestFreeSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestFreeSpec.kt)
* [KotestWordSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestWordSpec.kt)
* [KotestFeatureSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestFeatureSpec.kt)
* [KotestExpectSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestExpectSpec.kt)
* [KotestAnnotationSpec.kt](app/src/test/java/com/example/learningtest/kotest/KotestAnnotationSpec.kt)

</details>

</details>

<details>
  <summary><span style="font-size: 2.0em; font-weight: bold;">📌 Pure Kotlin </span></summary>
Pure Kotlin 은 Kotlin 의 기본적인 문법을 학습한다.
    <details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 Data class </span></summary>

[TestDataClass.kt](app/src/test/java/com/example/learningtest/dataclass/TestDataClass.kt)

코틀린의 데이터클래스는 주로 데이터를 보관하는데 사용된다.
각 데이터 클래스에 대해 컴파일러는 자동으로 추가적인 멤버 함수를 생성한다:

- `equals()`
- `hashCode()`
- `toString()`
- `copy()`
- `componentN()`

코틀린에서 데이터 클래스는 안전하고 신뢰할 수 있는 코드를 작성하는 코틀린의 철학에 중요한 역할을 한다.  
**불변성을 촉진**하기 위해 데이터 클래스는 프로퍼티를 `val`로 선언하여 생성 후에는 변경할 수 없는 인스턴스를 만든다.  
`copy()` 메소드는 이를 보완하여 **원본 객체를 변경하지 않고 수정된 프로퍼티를 가진 새 인스턴스를 생성**할 수 있다.

이러한 접근은 **가변 상태로 인한 의도하지 않은 부작용을 방지**하고 코드 안전성을 향상시키며, **코틀린이 수용하는 함수형 프로그래밍 원칙과 일치**한다.  
불변 데이터 처리를 용이하게 하고 객체 복사에 대한 간결한 구문을 제공함으로써, 코틀린은 개발자가 명확하고 유지보수 가능하며 버그에 강한 코드를 작성할 수 있도록 한다.


</details>

<details>
  <summary><span style="display: inline-block; font-size: 1.5em; font-weight: bold;">📌 sealed class</span></summary>

sealed class & interface 는 클래스 계층 구조의 제어된 상속을 제공한다.  
sealed class 의 모든 직접 하위 클래스는 컴파일 시간에 알려진다.  
즉, 상위 클래스가 직접 하위 클래스에 대해 알고 있는 것이다.  
다른 하위 클래스들은 상위 클래스가 알지 못한다.

### 목적

고정된 하위 클래스 집합을 가진 제한된 클래스 계층 구조를 나타내기 위함.

* 특징:
    * 모든 직접 하위 클래스는 동일한 파일에 선언되어야 한다.
    * 컴파일러는 모든 가능한 하위 클래스를 알기 때문에 `when` 표현식을 통해 타입 안전성을 향상시킨다.
    * 추상 멤버를 가질 수 있으며 데이터 클래스나 객체 선언으로 확장될 수 있다.

### 사용 시나리오

* 제한된 클래스 상속이 필요한 경우:
    * 컴파일 시간에 알려진 미리 정의된 유한 집합의 하위 클래스가 있는 클래스를 확장하고 싶은 경우.
* 타입 안전한 디자인이 필요한 경우:
    * 프로젝트에서 안전성과 패턴 매칭이 중요한 경우. 특히 상태 관리나 복잡한 조건 로직을 처리하는 경우.
      예를 들어, `when` 표현식을 사용한 sealed class 사용을 참조하십시오.
* 닫힌 API와 작업:
    * 제 3자 클라이언트가 의도된 대로 API를 사용하도록 보장하는 견고하고 유지 관리 가능한 라이브러리의
      공개 API가 필요한 경우.

### More concrete example for android

* [API 요청-응답 핸들링 시나리오](app/src/test/java/com/example/learningtest/sealedclass/TestSealedClassUserService.kt)
* [UI 상태 핸들링 시나리오](app/src/test/java/com/example/learningtest/sealedclass/TestSealedClassUiState.kt)
* 또한 아래와 같은 비즈니스 로직을 처리하는 것도 가능하다.

```kotlin
sealed class Payment {
    data class CreditCard(val number: String, val expiryDate: String) : Payment()
    data class PayPal(val email: String) : Payment()
    data object Cash : Payment()
}

fun processPayment(payment: Payment) {
    when (payment) {
        is Payment.CreditCard -> processCreditCardPayment(payment.number, payment.expiryDate)
        is Payment.PayPal -> processPayPalPayment(payment.email)
        is Payment.Cash -> processCashPayment()
    }
}
```

</details>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 lateinit </span></summary>

lateinit은 Kotlin에서 클래스 속성과 최상위 속성에 사용할 수 있는 수정자(modifier)입니다.
즉, var 속성을 즉시 값을 제공하지 않고 나중에 초기화할 수 있도록 해줍니다.

* 제약 사항:
  • 기본 타입(Int, Double 등)에는 사용할 수 없음
  • null이 될 수 없는 타입(String, Int가 아닌 String? 같은 타입은 불가능)
  • Android 개발에서 객체 생성 시 즉시 초기화할 수 없지만, 사용 전에 반드시 초기화되는 속성에 자주 사용됨

### Android 개발에서 lateinit의 실제 사용 사례

1. View Binding을 즉시 초기화하지 않고 사용
   TextView, Button 등의 UI 요소는 setContentView()가 호출된 후 onCreate()에서 초기화해야 함.

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView.text = "Hello, World!"
    }

}
```

2. 생명주기(Lifecycle)에 맞춘 초기화
   Android 생명주기에 따라 초기화가 필요한 ViewModel, Fragment 속성 등에 사용됨.

```kotlin
class MyFragment : Fragment() {
    private lateinit var adapter: MyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyAdapter()
    }
}
```

3. 의존성 주입(Dependency Injection)과 함께 사용
   Hilt 또는 Dagger 같은 DI 프레임워크에서 lateinit을 활용할 수 있음.👇

```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun user(): User = userRepository.user()

}
```

### lateinit의 단점

1. var 속성에서만 사용 가능:
    * val(불변 속성)에서는 사용할 수 없음
2. 기본적으로 스레드 안전(Thread-Safe)하지 않음
    * 여러 스레드에서 동시에 접근할 경우, 예상치 못한 상태나 충돌 발생 가능
3. 코드 스멜(Code Smell)과 잘못된 사용 가능성
    * 초기화를 미루다가 NullPointerException이 발생할 위험이 있음
    * 예제: lateinit 속성을 초기화하기 전에 접근하면 런타임 예외 발생👇

```kotlin
lateinit var name: String

fun printName() {
    println(name) // 초기화되지 않으면 예외 발생
}
```

### 테스트에서의 lateinit 사용

📌 [LateinitTest.kt](app/src/test/java/com/example/learningtest/lateinit/LateinitTest.kt), [LateinitAndroidComponentTest.kt](app/src/test/java/com/example/learningtest/lateinit/LateinitAndroidComponentTest.kt)
같은 파일에서 테스트 목적으로 활용됨.

### 결론

* lateinit은 Android 개발에서 UI 요소, 의존성 주입, 생명주기 관리에 유용하지만,
* 남용하면 NullPointerException 발생 위험이 있으므로 신중하게 사용해야 함!

</details>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 by &compare by lazy with lateinit </span></summary>

### by 키워드

by 키워드는 위임(Delegation) 을 위한 키워드로, 클래스가 특정 기능의 구현을 다른 객체에 위임 할 수 있도록 해줍니다.  
이를 통해 데코레이터 패턴(Decorator Pattern) 을 쉽게 구현할 수 있습니다.

* by 키워드를 사용한 위임의 종류:
    * [프로퍼티 위임(Property Delegation)](app/src/test/java/com/example/learningtest/by/ByPropertyDelegationTest.kt)
    * [인터페이스 위임(Interface Delegation)](app/src/test/java/com/example/learningtest/by/ByInterfaceDelegationTest.kt)

### 데코레이터 패턴(Decorator Pattern)

클래스의 기능을 확장하고 싶지만, 상속이 어려운 경우 데코레이터 패턴 을 사용할 수 있습니다.  
이 패턴의 핵심은 원래 클래스와 같은 인터페이스를 구현하는 새로운 클래스를 만들고, 원래 클래스의 인스턴스를 필드로 저장 하는 것입니다.  
그러나 이 방식은 보일러플레이트 코드 가 많아질 수 있습니다.

위임을 사용하지 않고 수동으로 데코레이터 패턴
구현 : [수동 데코레이터 패턴 예제](app/src/test/java/com/example/learningtest/by/ByInterfaceDelegationTest.kt))

### by 키워드를 사용한 위임의 단점

* 성능 오버헤드 (경미함): 위임을 사용하면 메서드 호출이 한 단계 추가 되어 성능 오버헤드가 발생할 수 있음

### Android 개발에서 by 키워드 사용 사례

* [Activity에서 ViewModel 초기화](app/src/test/java/com/example/learningtest/by/android/CreateViewModelLazilyTest.kt)
    * Android의 ViewModel을 액티비티의 생명주기에 맞게 생성 및 유지
    * by viewModels() 를 사용하면 ViewModelProvider를 직접 사용할 필요 없음

* [싱글톤 또는 무거운 리소스 초기화 (예: Retrofit)](app/src/test/java/com/example/learningtest/by/android/CreateRetrofitLazilyTest.kt)
    * 네트워크 요청을 위한 Retrofit 인스턴스를 초기화할 때 by lazy를 활용하면 앱 실행 속도를 개선
    * 객체를 처음 사용할 때만 초기화됨

* View Binding 초기화
    * by lazy 를 활용하면 뷰가 생성될 때만 바인딩 객체가 초기화 됨
    * 불필요한 메모리 사용을 줄일 수 있음👇

```kotlin
class StubActivity : ComponentActivity() {
    private val binding: ActivityStubBinding by lazy {
        ActivityStubBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView.text = "Hello, Android!"
    }

}
```

### by lazy

`by lazy` 는 프로퍼티가 처음 접근될 때 초기화되는 Kotlin의 위임(delegate) 기능 입니다.

* 장점:
    * 성능 최적화: 불필요한 초기화를 방지하여 앱 실행 속도 개선
    * 리소스 관리: 사용되지 않는 객체의 불필요한 메모리 점유 방지
    * 스레드 안전(Thread Safety): 기본적으로 LazyThreadSafetyMode.SYNCHRONIZED 적용

### lateinit vs by lazy

| 속성             | `lateinit`                                | `by lazy`              |
|----------------|-------------------------------------------|------------------------|
| 같이 쓰이는 키워드     | `var`                                     | `val`                  |
| 초기화되는 때        | 첫 사용 이전에 반드시 초기화해야 함                      | 첫 접근할 때 자동으로 초기화됨      |
| Null 가능성       | `null`로 사용할 수 없음                          | nullable 타입으로 사용 가능    |
| 사용 범위          | 클래스 프로퍼티에서만 사용 가능                         | 클래스 프로퍼티 & 로컬 변수 사용 가능 |
| 원시 타입 사용 가능 여부 | 불가능                                       | 가능                     |
| 스레드 안전성        | 기본적으로 스레드-안전하지 않음                         | 기본적으로 스레드-안전함          |
| 초기화되지 않았을 때 에러 | `UninitializedPropertyAccessException` 발생 | -                      |

</details>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 Nested vs inner </span></summary>

## 중첩(Nested) 클래스와 내부(Inner) 클래스 (기본적으로 Nested)

Kotlin에서는 클래스 안에 다른 클래스를 선언할 수 있습니다.

### nested 클래스 (중첩 클래스) in Kotlin

nested 클래스는 inner 키워드 없이 다른 클래스 내부에 선언된 클래스입니다.  
기본적으로 정적(static) 클래스 로 간주되며, 외부 클래스의 멤버에 접근할 수 없습니다.

* 언제 사용해야 할까?
    * 외부 클래스의 멤버에 접근할 필요가 없고, 단순히 관련된 클래스를 그룹화하고 싶을 때 사용

### inner 클래스 (내부 클래스) in Kotlin

inner 클래스는 inner 키워드가 붙은 중첩 클래스입니다.  
외부 클래스의 인스턴스를 참조할 수 있으며, 외부 클래스의 멤버에도 접근할 수 있습니다.

* 언제 사용해야 할까?
    * 외부 클래스의 멤버를 접근해야 할 경우

### Java에서는?

Java에서는 기본적으로 내부 클래스(inner class)가 기본 설정 입니다.  
즉, Java에서 클래스 내부에 클래스를 선언하면, 기본적으로 외부 클래스의 멤버에 접근 가능 합니다.

만약 외부 클래스와 독립적인 정적 클래스 로 만들고 싶다면, static 키워드를 붙여야 합니다.

📌 즉,

* Kotlin에서는 nested(중첩) 클래스가 기본적으로 static
* Java에서는 inner(내부) 클래스가 기본이며, static을 명시적으로 붙여야 중첩 클래스가 됨

![nested vs inner.png](app/src/test/java/com/example/learningtest/nestedAndInner/nested%20vs%20inner.png)

### Android 개발에서의 inner 클래스 예제

* [안드로이드 Inner 클래스 예제](app/src/test/java/com/example/learningtest/nestedAndInner/InnerClassAndroidExample.kt)
    * 내부 클래스가 외부 클래스의 속성 및 함수를 조작할 필요가 있을 때
    * 대표적인 예: 바운드 서비스(Bound Service)
    * Binder 클래스가 서비스의 메서드와 데이터를 참조해야 할 때 내부 클래스로 사용됨
* 안드로이드 브로드캐스트 리시버 (BroadcastReceiver)
    * 특정 동작을 감지하고 외부 클래스의 데이터를 조작 해야 할 때 inner 클래스를 사용 가능

### Android 개발에서의 nested 클래스 예제

* AndroidNestedClassStubTest.kt
    * 관련 있는 클래스들을 논리적으로 그룹화 하지만, 외부 클래스의 인스턴스를 필요로 하지 않는 경우
    * 대표적인 예: UI 상태 모델링 (sealed class 기반 상태 관리), 이벤트 클래스, ViewModel이나 Activity 내에서 헬퍼(Helper) 역할을 하는
      유틸리티 객체

Kotlin에서는 기본적으로 nested(중첩) 클래스이므로, 외부 클래스의 멤버를 참조해야 한다면 inner 키워드를 명시적으로 붙여야 합니다!

</details>

</details>

<details>

  <summary><span style="font-size: 2.0em; font-weight: bold;">📌 Android </span></summary>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 Activity </span></summary>

## Activity

### Activity란 무엇인가? (간단 설명)

Activity는 기본적으로 앱의 한 화면을 나타내는 구성 요소 입니다.  
사용자가 화면을 보고, 터치하고, 상호작용하는 공간 이며, 현재 중요한 정보를 표시하는 역할을 합니다.

또한, 앱이 일시 정지(Paused)되거나 중단(Stopped)되었을 때 상태를 저장 및 복원 할 수 있도록 관리합니다.  
Activity를 사용하면 앱 간 데이터를 공유하거나, 특정 화면으로 이동하는 것이 더 쉬워집니다.

### 왜 Activity를 사용해야 하는가?

일반적인 프로그래밍에서는 main() 함수 를 호출해 프로그램이 시작되지만,  
Android에서는 시스템이 Activity의 생명주기에 맞춰 특정 콜백 메서드 를 실행하여 앱을 실행합니다.

즉, Activity는 사용자가 앱을 실행할 때 자동으로 Android 시스템에 의해 관리되며,  
생명주기 내에서 적절한 이벤트 처리 를 할 수 있도록 도와줍니다.

### UI를 표시하지 않는 Activity (setContentView 없이)

Activity는 반드시 UI를 가질 필요가 없습니다.  
UI를 표시하지 않는 Activity는 앱 내에서 “트래픽 컨트롤러”(Front Controller) 역할을 할 수 있습니다.

예를 들어, 데이터 또는 Intent를 받아서 다른 Activity나 Fragment로 즉시 전달하는 역할 을 수행할 수 있습니다.

### UI 없는 Activity를 사용할 때의 이점?

* 네비게이션을 단순화할 수 있음
* 코드를 더 깔끔하게 유지할 수 있음
* 사용자의 흐름(로그인 여부, 튜토리얼 완료 여부 등)을 하나의 Activity에서 관리 가능
* 예: 사용자가 앱을 실행했을 때,
    * 로그인 여부 확인 후 → 로그인 화면 또는 메인 화면으로 이동
    * 첫 방문자인 경우 → 튜토리얼 화면으로 이동

이처럼 UI 없이도 앱의 흐름을 제어하는 역할 을 할 수 있습니다.

</details>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 context </span></summary>

## Android Context

Android 앱을 개발할 때 Context 클래스는 항상 만나게 됩니다.  
Context는 안드로이드 개발에서 매우 중요한 개념 이며,  
Context 없이는 Activity 시작, Broadcast 송출, 서비스 실행 등을 수행할 수 없습니다.

따라서 Context의 개념을 이해하면 안드로이드 컴포넌트(Activity, Service, Broadcast Receiver, Content Provider)의 동작 원리를
이해하는 데 도움이 됩니다.  
각 컴포넌트는 Context를 통해 시스템 서비스 및 앱 리소스에 접근 할 수 있습니다.

### Android Context란?

Context는 현재 애플리케이션의 상태 정보를 제공하는 인터페이스 입니다.  
즉, 앱 환경 및 시스템 리소스에 접근할 수 있도록 해주는 역할 을 합니다.

* Context가 제공하는 주요 기능
    * 리소스 접근: getResources(), getString(), getDrawable() 등
    * 시스템 서비스 접근: getSystemService()
    * Intent 실행: startActivity(), startService()
    * 레이아웃 인플레이션: LayoutInflater 를 사용하여 XML을 View로 변환

### Context와 그 하위 클래스

Context 는 추상 클래스 로 존재하며,
이를 확장하는 `ContextWrapper` 와 `ContextImpl` 클래스가 있습니다.

* ContextWrapper 는 ContextImpl 인스턴스를 참조하며,
* Activity, Service, Application 클래스는 ContextWrapper 의 구체적인 구현체 입니다.

![context-hierarchy-diagram.png](app/src/androidTest/java/com/example/learningtest/context/context-hierarchy-diagram.png)

위 다이어그램은 Context, ContextWrapper, ContextImpl 의 관계를 나타냅니다.

### Context를 얻는 다양한 방법

#### Activity에서 Context를 얻는 방법

1. 현재 액티비티(Context) 사용 `this`
2. Base Context 얻기 (Activity 내부에서) `getBaseContext()`
3. Application Context 얻기 `getApplicationContext()`

이처럼 여러 방식으로 Context를 얻을 수 있으며, 상황에 맞는 적절한 Context를 선택해야 합니다.

#### Application Context VS Activity Context

|           | Application Context | Activity Context                   |
|-----------|---------------------|------------------------------------|
| 생명주기      | 앱 전체에 묶여있음          | 특정 액티비티에 묶여있음                      |
| 사용 범위     | 글로벌(앱 전체에서 사용 가능)   | 특정 Activity에서만 사용 가능               |
| UI 관련 작업  | 적합하지 않음             | 적합함                                |
| 메모리 누수 위험 | 낮음                  | 높음 (잘못 사용하면 메모리 누수 발생 가능)          |
| 사용 예제     | 싱글톤 객체 (Database 등) | `Dialog`, `Snackbar`, 애니메이션 및 뷰 관리 |

* Application Context를 Activity Context 대신 사용하면 문제 발생 가능!
* Application Context에서는 정상 동작
* `Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show()`
* Application Context에서는 예외 발생 👇

```kotlin
AlertDialog.Builder(getApplicationContext())
    .setTitle("Title")
    .setMessage("Message")
    .show() // 예외 발생!
```

즉, UI 관련 작업에서는 Activity Context를 사용해야 합니다!

### Context 오용 사례 및 주의점

1. Activity Context를 잘못 관리하여 메모리 누수 발생
   Activity Context를 정적(static) 변수나 싱글톤(Singleton)에서 참조하지 말 것.
   이렇게 하면 Activity가 가비지 컬렉션(garbage collection)되지 못하게 되어 메모리 누수가 발생할 수 있음.

   문제 코드 (메모리 누수 발생 가능)

   ```kotlin
   object Singleton {
       var context: Context? = null
   }
   Singleton.context = this // Memory leak!
   ```

   해결책: 대신에 Application Context 사용:

   ```kotlin
   Singleton.context = applicationContext
   ```

2. Application Context 를 UI 작업에 사용하는 경우 👎
   Application Context는 UI 요소(Dialog, View 등)를 직접 조작할 수 없습니다.

   ```kotlin
   val view = LayoutInflater.from(applicationContext).inflate(R.layout.activity_main, null)
   ```

   💡 해결 방법:

   // ✅ Activity의 Context 사용 -> UI 속성 유지
   ```kotlin
   val view = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
   ```

3. Context를 직접 인스턴스화하려는 경우
   Context는 시스템이 관리하는 클래스이므로 직접 인스턴스화할 수 없음
   ```kotlin
    // ❌ Context를 직접 생성할 수 없음
    val myContext = Context()
   ```
   💡 해결 방법:
   Activity, Service, Application 등의 Context를 활용해야 함

### 학습 테스트

[ContextBasicTest.kt](app/src/test/java/com/example/learningtest/context/ContextBasicTest.kt)

### 결론

Context를 올바르게 이해하고 사용해야 안정적이고 효율적인 Android 앱을 개발할 수 있음

* Context의 생명주기와 역할을 이해해야 함
* 앱 전체에서 사용할 작업은 Application Context를 활용
* UI 관련 작업은 Activity Context를 활용
* 메모리 누수 방지를 위해 Static 변수에 Activity Context 저장 금지

Context를 적절히 사용하면 앱이 더욱 견고하고 유지보수하기 쉬워집니다!

참고 자료
• ContextBasicTest.kt
• Android 공식 문서: Context
• 책: 안드로이드 프로그래밍 Next Step - 노재춘

</details>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 basic compose codelab </span></summary>

# **Compose 기본 Codelab 학습**

```kotlin
class BasicComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}
```

`setContent`을 사용하여 레이아웃을 정의하지만, 기존 View 시스템에서 `setContentView`를 사용하여 XML을 적용하는 것과 달리,
**Composable 함수**를 직접 호출하여 UI를 구성합니다.

`LearningTestTheme`은 Composable 함수의 스타일을 정의하는 역할을
합니다. [Theme.kt](app/src/main/java/com/example/learningtest/ui/theme/Theme.kt)

학습
테스트: [GreetingV1KtTest.kt](app/src/androidTest/java/com/example/learningtest/compose/basic/codelab/GreetingV1KtTest.kt)

---

## **Surface**

`Surface`를 사용하여 `Greeting`의 배경 색상을 변경할 수 있습니다.
`Surface`는 `color` 속성을 가지므로 `MaterialTheme.colorScheme.primary`를 사용할 수 있습니다.

`Surface` 내부에 있는 컴포넌트들은 해당 배경색 위에 그려집니다.
[GreetingV2.kt](app/src/main/java/com/example/learningtest/compose/basic/codelab/GreetingV2.kt):
`GreetingV2`는 `Surface`를 사용합니다.

**텍스트 색상을 확인해 보세요.**

- 명시적으로 텍스트 색상을 지정하지 않았지만, 흰색으로 나타납니다.
- `Surface`가 `primary` 색상을 배경으로 설정하면, 그 위에 그려지는 텍스트는 자동으로 `onPrimary` 색상을 사용합니다.

이러한 기능은 `Material` 디자인이 **의견(opinionated) 있는 디자인 시스템**이기 때문입니다.
즉, **일반적인 UI 패턴을 미리 고려하여 기본적인 설정을 제공**합니다.

Material 컴포넌트는 `androidx.compose.foundation` 위에 구축되었으며,
더 많은 유연성이 필요하면 해당 요소들을 직접 사용할 수도 있습니다.

학습
테스트: [GreetingsV2KtTest.kt](app/src/androidTest/java/com/example/learningtest/compose/basic/codelab/GreetingsV2KtTest.kt)

---

## **Modifier**

Compose UI 요소(`Surface`, `Text` 등)는 **선택적 Modifier 매개변수**를 가질 수 있습니다.  
`Modifier`는 UI 요소의 **레이아웃, 크기, 동작을 정의하는 역할**을 합니다.

예를 들어, `padding` Modifier를 사용하면 요소 주변에 여백을 추가할 수 있습니다.

```kotlin
Modifier.padding(24.dp)
```

여러 Modifier를 체이닝하여 사용할 수도 있습니다.

[GreetingV3.kt](app/src/main/java/com/example/learningtest/compose/basic/codelab/GreetingV3.kt):
`GreetingV3`는 `Modifier.padding()`과 `Surface`를 사용합니다.

**Modifier의 주요 역할**

- `Composable`의 크기, 레이아웃, 동작 및 외형 변경
- 접근성 정보 추가
- 사용자 입력 처리
- 클릭 가능, 스크롤 가능, 드래그 가능 등의 고수준 상호작용 추가

---

## **Composable 재사용하기**

UI를 구성할 때 **작고 재사용 가능한 Composable을 만들어야 유지보수가 쉬워집니다.**  
각 컴포넌트는 **화면의 일부만 담당하며 독립적으로 수정할 수 있어야 합니다.**

**베스트 프랙티스**

- `Modifier` 매개변수를 기본값 `Modifier`로 설정하고,
- 해당 `Modifier`를 첫 번째 Composable에 전달하는 것이 좋습니다.

이렇게 하면, **Composable을 호출하는 쪽에서 원하는 동작을 추가로 지정할 수 있습니다.**

`MyApp` Composable을 만들어 `Greeting`을 포함하세요.  
[BasicComposeActivity.kt](app/src/main/java/com/example/learningtest/compose/basic/codelab/BasicComposeActivity.kt), [하단에 GreetingV3Preview 함수](app/src/main/java/com/example/learningtest/compose/basic/codelab/GreetingV3.kt)
는 `MyApp`을 재사용합니다.

---

## **Column과 Row**

Compose의 기본적인 레이아웃 요소는 `Column`, `Row`, `Box`입니다.
![기본 레이아웃 요소](app/src/main/java/com/example/learningtest/compose/basic/codelab/Basic-standard-layout-elements-in-Compose.png)

- `Column`: 요소들을 **수직으로 배치**
- `Row`: 요소들을 **수평으로 배치**
- `Box`: **겹쳐서 배치**

[GreetingV4.kt](app/src/main/java/com/example/learningtest/compose/basic/codelab/GreetingV4.kt):
`Column`을 사용합니다.

```kotlin
Column {
    for (i in 1..5) {
        Text("Item $i")
    }
}
```

**테스트**: `Row` 또는 `Column`의 하위 요소를 검사하려면 `Modifier.testTag("RowTag")`를 추가해야 합니다.

학습
테스트: [GreetingV4KtTest.kt](app/src/androidTest/java/com/example/learningtest/compose/basic/codelab/GreetingV4KtTest.kt)

---

## **ElevatedButton 추가하기**

[GreetingV5.kt](app/src/main/java/com/example/learningtest/compose/basic/codelab/GreetingV5.kt)

- `Column`을 포함하는 `Row`를 추가하고,
- `Row` 내부에서 `Column`에 `Modifier.weight(1f)`를 적용
- `ElevatedButton`은 weight 없이 추가

```kotlin
Row {
    Column(modifier = Modifier.weight(1f)) {
        Text("Hello")
    }
    ElevatedButton(onClick = { /* TODO */ }) {
        Text("Show more")
    }
}
```

- `weight(1f)` 적용 시 효과:
    - `Column`이 **Row의 남은 공간을 모두 차지**
    - `ElevatedButton`은 필요한 공간만 차지

**테스트**: `Row`와 `Column`이 합쳐질 수 있으므로, `Modifier.testTag("RowTag")`를 추가하여 테스트에 반영해야 합니다.

학습
테스트: [GreetingV5KtTest.kt](app/src/androidTest/java/com/example/learningtest/compose/basic/codelab/GreetingV5KtTest.kt)

---

## **Compose에서 상태(State) 관리**

```kotlin
var expanded: Boolean = false
ElevatedButton(
    onClick = { expanded = !expanded }
) {
    Text(if (expanded) "Show less" else "Show more")
}
```

- 이 코드가 작동하지 않는 이유**
    - `expanded` 값이 변경되어도 **Compose는 이를 상태 변화로 인식하지 않음**
    - `Greeting`이 다시 호출될 때마다 `expanded`가 **항상 `false`로 초기화됨**

- **해결 방법**
    - `mutableStateOf`와 `remember`를 사용하여 상태를 추적

```kotlin
val expanded = remember { mutableStateOf(false) }
```

**rememberSaveable**을 사용하면 회전 등으로 인한 상태 초기화를 방지할 수 있습니다.

학습
테스트: [GreetingV6KtTest.kt](app/src/androidTest/java/com/example/learningtest/compose/basic/codelab/GreetingV6KtTest.kt)

---

## **Lazy List (리스트 최적화)**

Compose는 **`LazyColumn`과 `LazyRow`를 제공**하여 RecyclerView처럼 대량 데이터를 효율적으로 렌더링할 수 있습니다.

```kotlin
LazyColumn {
    items(1000) { index ->
        Text("Item $index")
    }
}
```

**`LazyColumn`은 RecyclerView처럼 뷰를 재활용하지 않지만, Composable 생성을 최소화하여 성능을 최적화합니다.**

---

## **애니메이션 적용하기**

Compose에서는 다양한 애니메이션 API를 제공합니다.  
`spring`을 사용하면, 시간 기반 애니메이션이 아닌 물리 기반 애니메이션을 적용할 수 있습니다.

**자세한 내용:** [Compose 애니메이션 문서](https://developer.android.com/codelabs/jetpack-compose-basics#5)

---

**정리**

- `setContent`를 사용하여 Composable 기반 UI 생성
- `Modifier`를 활용하여 레이아웃, 스타일 적용
- `remember`, `rememberSaveable`을 사용하여 상태 관리
- `LazyColumn`을 활용하여 성능 최적화
- `spring`을 이용한 자연스러운 애니메이션 적용

</details>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 Compose 상태 관리 Codelab 학습 </span></summary>

# Compose 상태 관리 Codelab 학습

앱의 **상태(State)** 는 UI에 표시할 내용을 설명하며, **이벤트(Events)** 는 상태를 변경하는 메커니즘입니다.
상태가 변경되면 UI가 다시 그려집니다.

**핵심 개념:**

- **상태는 존재한다. 이벤트는 발생한다.**

모든 Android 앱에는 **UI 업데이트 루프** 가 있으며, 기본적인 흐름은 다음과 같습니다.

![UI 업데이트 루프](app/src/main/java/com/example/learningtest/compose/state/docs/Ui-update-loop.png)

---

## **잘못된 상태 변경 방식**

```kotlin
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        var count = 0
        Text("You've had $count glasses.")
        Button(onClick = { count++ }, Modifier.padding(top = 8.dp)) {
            Text("Add one")
        }
    }
}
```

**이 코드가 동작하지 않는 이유**

- `count` 값이 변경되어도 **Compose가 이를 상태 변경으로 인식하지 않음**
- 매번 `WaterCounter`가 다시 호출될 때, `count`가 **항상 0으로 초기화됨**

---

## **Composable 함수에서의 메모리 관리**

**Compose의 UI 렌더링 개념**

- **Composition**: Composable이 실행될 때 생성되는 UI의 설명
- **초기 Composition**: 처음 Composable이 실행될 때 Composition이 생성됨
- **Recomposition**: 데이터 변경 시 필요한 Composable만 다시 실행됨

Compose는 **상태를 추적** 하여 필요한 UI 요소만 다시 그립니다.
이를 위해 `mutableStateOf` 함수를 사용하여 **관찰 가능한 `MutableState`를 생성**해야 합니다.

---

## **remember를 활용한 상태 저장**

**`remember`는 Composition에 객체를 저장하며, Composition이 다시 실행되지 않으면 상태를 유지합니다.**

```kotlin
val count = remember { mutableStateOf(0) }
```

[WaterCounterV2.kt](app/src/main/java/com/example/learningtest/compose/state/WaterCounterV2.kt) 참고

### **1. 초기 상태**

![초기 상태](app/src/main/java/com/example/learningtest/compose/state/docs/1WaterCounterV2-initial-state%20.png)

### **2. 'Add one' 버튼 클릭**

![버튼 클릭](app/src/main/java/com/example/learningtest/compose/state/docs/2WaterCounterV2-click-add-one-button.png)

### **3. 'Clear' 버튼 클릭 후 다시 'Add one' 버튼 클릭**

![초기화 후 추가](app/src/main/java/com/example/learningtest/compose/state/docs/5WaterCounterV2-click-clear-water-count-button.png)

---

## **Compose에서 상태 복원**

**`rememberSaveable`을 사용하여 Activity가 재생성된 후에도 상태를 유지할 수 있습니다.**

```kotlin
val count = rememberSaveable { mutableStateOf(0) }
```

`rememberSaveable`은 **구성 변경(예: 화면 회전) 및 시스템에 의해 프로세스가 종료된 후에도 상태를 유지** 합니다.

---

## **상태 끌어올리기 (State Hoisting)**

상태를 `remember`로 저장하는 Composable은 **내부 상태를 가지는(stateful) Composable** 입니다.  
**내부 상태를 가지는 Composable은 재사용성이 낮고 테스트가 어려울 수 있습니다.**

**상태를 가지지 않는(Stateless) Composable을 만들기 위해 상태를 끌어올릴 수 있습니다.**

```kotlin
// Stateful Composable
@Composable
fun StatefulCounter() {
    var count by remember { mutableStateOf(0) }
    StatelessCounter(count, { count++ })
}
```

**State hoisting 이점**

- **단일 진실 원칙 (Single Source of Truth)**: 중복 없이 한 곳에서 상태 관리 가능
- **공유 가능 (Shareable)**: 상태를 여러 Composable에서 공유 가능
- **가로채기 가능 (Interceptable)**: 상태를 변경하기 전 이벤트를 수정 가능
- **디커플링 (Decoupled)**: 상태 저장 위치를 자유롭게 설정 가능 (예: ViewModel 활용)

**관련 파일** :
[Stateless 한 카운터](app/src/main/java/com/example/learningtest/compose/state/LiquidStatelessCounter.kt) , [Stateful 한 카운터](app/src/main/java/com/example/learningtest/compose/state/WaterStatefulCounter.kt)

---

## **목록(List) 관리**

**목록을 관리할 때, `mutableListOf()` 대신 `mutableStateListOf()`를 사용해야 합니다.**

```kotlin
val list = remember { mutableStateListOf<WellnessTask>().apply { addAll(getWellnessTasks()) } }
```

**목록 상태 복원:** `rememberSaveable`을 사용하여 목록의 상태를 유지할 수 있습니다.

```kotlin
val list = rememberSaveable { mutableStateListOf<WellnessTask>() }
```

**관련 파일**:
[WellnessTask.kt](app/src/main/java/com/example/learningtest/compose/state/WellnessTask.kt),[WellnessTasksListV2.kt](app/src/main/java/com/example/learningtest/compose/state/WellnessTasksListV2.kt)

---

## **ViewModel에서 상태 관리하기**

ViewModel은 **UI 상태를 제공하고, 비즈니스 로직과의 연결을 관리** 합니다.

**ViewModel을 활용하면:**

- UI 상태를 앱의 다른 계층과 연결 가능
- 구성 변경에도 상태 유지 가능
- Composition 외부에서 상태 관리 가능

```kotlin
@HiltViewModel
class WellnessViewModel @Inject constructor() : ViewModel() {
    private val _tasks = MutableLiveData<List<WellnessTask>>()
    val tasks: LiveData<List<WellnessTask>> get() = _tasks
}
```

**관련 파일**:
[WellnessViewModel.kt](app/src/main/java/com/example/learningtest/compose/state/WellnessViewModel.kt)

---

## **상태 변경 감지**

**MutableList의 특정 속성이 변경될 때도 Compose가 이를 감지할 수 있도록 해야 합니다.**

**잘못된 코드** (Compose가 변경을 감지하지 못함)

```kotlin
data class WellnessTask(
    val id: Int,
    val label: String,
    var checked: Boolean = false,
)
```

**올바른 코드** (Compose가 변경을 감지할 수 있도록 `MutableState` 사용)

```kotlin
data class WellnessTask(
    val id: Int,
    val label: String,
    val checked: MutableState<Boolean> = mutableStateOf(false)
)
```

**관련 파일**:
[WellnessTask.kt](app/src/main/java/com/example/learningtest/compose/state/WellnessTask.kt)

---

**정리**

- `remember`를 사용하여 상태를 유지
- `rememberSaveable`을 사용하여 구성 변경 후에도 상태 유지
- 상태를 끌어올려(State Hoisting) 재사용성을 높이고 유지보수를 쉽게 만듦
- `ViewModel`을 활용하여 UI 상태를 관리
- `mutableStateListOf()`를 사용하여 리스트의 변경을 감지

**더 알아보기**:
[Jetpack Compose 상태 관리 문서](https://developer.android.com/codelabs/jetpack-compose-state#11)



</details>

</details>

<details>

  <summary><span style="font-size: 2.0em; font-weight: bold;">📌 OOP </span></summary>

<details>

  <summary><span style="font-size: 1.5em; font-weight: bold;">📌 SOLID </span></summary>

## SOLID 원칙

* SOLID는 객체 지향 설계를 더욱 이해하기 쉽고, 확장 가능하며 유지보수가 용이하도록 만드는 5가지 원칙 을 의미합니다.
    * SRP (단일 책임 원칙, Single Responsibility Principle)
    * OCP (개방-폐쇄 원칙, Open-Closed Principle)
    * LSP (리스코프 치환 원칙, Liskov Substitution Principle)
    * ISP (인터페이스 분리 원칙, Interface Segregation Principle)
    * DIP (의존성 역전 원칙, Dependency Inversion Principle)

### SRP (Single Responsibility Principle, 단일 책임 원칙)

“모듈(클래스, 함수, 패키지 등)은 오직 하나의 책임만 가져야 한다”  
즉, 하나의 모듈(클래스 등)이 변경되는 이유는 단 하나여야 합니다.

#### SRP 위반 예제

[SRP 위반 예제](app/src/test/java/com/example/learningtest/solid/SRPViolated.kt)SRP 위반 예제
LottoSeller 클래스 코드에서 LottoSeller 는 너무 많은 역할을 담당 하고 있습니다.

* 로또 개수 계산
* 랜덤 번호 생성
* 로또 번호 검증

#### 문제점:

로또 번호 생성 전략이 바뀌면 LottoSeller 를 변경해야 합니다.  
하지만 로또 번호 생성 전략은 LottoSeller 의 책임이 아닙니다!  
즉, 이 클래스는 여러 이유로 변경될 가능성이 있으므로 SRP를 위반하고 있습니다.

#### SRP 준수 (리팩토링)

[SRP 준수 (리팩토링)](app/src/test/java/com/example/learningtest/solid/SRPRefactored.kt)SRP 준수 (리팩토링)
리팩토링된 코드에서는 `LottoSeller` 가 가격만 계산하도록 역할을 분리했습니다.

* 로또 번호 생성 책임 → `LotteryGenerateStrategy` 로 이동
* 로또 번호 검증 책임 → `Lottery` 클래스가 처리
* 결과:
    * 각 클래스는 오직 하나의 변경 이유만 가지게 됨
    * 이 원칙은 클래스뿐만 아니라, 모듈, 패키지, 함수에도 적용 가능

#### OCP (Open-Closed Principle, 개방-폐쇄 원칙)

“소프트웨어 요소(클래스, 모듈, 함수 등)는 확장에는 열려 있어야 하고, 변경에는 닫혀 있어야 한다”
즉, 새로운 기능을 추가할 때 기존 코드를 수정하지 않고도 기능을 확장할 수 있어야 합니다.

#### OCP 위반 예제

[OCP 위반 예제](app/src/test/java/com/example/learningtest/solid/OCPViolated.kt)

고객(Customer)이 LottoSeller 에서 로또를 구매한다고 가정합니다.

```kotlin
private class Customer {
    fun buyLotto(money: Int, lottoSeller: LottoSeller): List<Lottery> {
        return lottoSeller.soldLotto(money)
    }
}
```

이제 “할인된 로또 판매점(DiscountedLottoSeller)” 이 추가된다고 가정해 봅시다.  
OCP 위반 코드에서는 Customer 클래스 내부 코드를 변경해야 합니다.

* 문제점:
    * 새로운 판매점이 추가될 때마다 Customer 클래스를 수정해야 함
    * OCP 원칙에 따르면, 새로운 기능이 추가될 때 기존 코드를 변경하지 않아야 함

#### OCP 준수 (리팩토링)

[OCP 준수 (리팩토링)](app/src/test/java/com/example/learningtest/solid/OCPRefactored.kt)

* 리팩토링된 코드에서는 인터페이스를 도입하여 문제를 해결 합니다.
    * LottoSeller 인터페이스를 생성
    * NormalLottoSeller 와 DiscountedLottoSeller 는 LottoSeller 인터페이스를 구현

* 결과:
    * Customer 클래스는 변경 없이 새로운 판매점 추가 가능
    * OCP 원칙을 준수하여 확장은 열려 있고, 기존 코드 변경은 필요 없음

### LSP (Liskov Substitution Principle, 리스코프 치환 원칙)

“상위 클래스(부모 클래스)를 하위 클래스(자식 클래스)로 대체하더라도 프로그램이 정상적으로 동작해야 한다”

#### LSP 위반 예제

[LSP 위반 예제](app/src/test/java/com/example/learningtest/solid/LSPViolated.kt)
로또 클래스(Lottery)에 “사각형(Rectangle)“과 “정사각형(Square)” 개념을 추가 해야 한다고 가정합니다.  
위반 코드를 보면, Square 클래스는 Rectangle 을 상속받습니다.

* 문제점:
    * Square 는 Rectangle 의 하위 클래스이지만, 기본적인 동작이 다름
    * Rectangle 에서는 setWidth(2), setHeight(5) 하면 면적은 10이 되어야 함
    * 하지만 Square 에서는 높이와 너비가 항상 같아야 하므로 면적이 25가 됨 (오류 발생)
    * 즉, Square 는 Rectangle 을 대체할 수 없으므로 LSP를 위반

#### LSP 준수 (리팩토링)

[LSP 준수 (리팩토링)](app/src/test/java/com/example/learningtest/solid/LSPRefactored.kt)

LSP 준수 코드에서는

* Rectangle 과 Square 의 관계를 부모-자식 상속 관계에서 일반 인터페이스(Shape)로 변경

* 결과:
    * Square 와 Rectangle 이 Shape 인터페이스를 따르게 변경
    * LSP 준수: 하위 클래스가 부모 클래스를 대체할 수 있음

### ISP (Interface Segregation Principle, 인터페이스 분리 원칙)

“클라이언트는 사용하지 않는 메서드에 의존하면 안 된다”  
즉, 불필요한 기능이 포함된 인터페이스를 강요해서는 안 됨

#### ISP 위반 예제

[ISP 위반 예제](app/src/test/java/com/example/learningtest/solid/ISPViolated.kt)

로또를 파는 사람(HumanLottoSeller)과 로또 자판기(MachineLottoSeller)가 있다고 가정합니다.  
ISP 위반 코드에서는 모든 로또 판매자는 reset() 과 chat() 을 구현해야 합니다.

* 문제점:
    * HumanLottoSeller 는 reset() 을 구현할 필요 없음
    * MachineLottoSeller 는 chat() 을 구현할 필요 없음
    * 불필요한 메서드 구현을 강요받으므로 ISP를 위반

#### ISP 준수 (리팩토링)

[ISP 준수 (리팩토링)](app/src/test/java/com/example/learningtest/solid/ISPRefactored.kt)

* 리팩토링 코드에서는
    * chat() 이 필요한 HumanLottoSeller 인터페이스
    * reset() 이 필요한 MachineLottoSeller 인터페이스
      로 분리하여 해결

* 결과:
    * 필요한 기능만 인터페이스로 나누어 ISP 준수
    * 불필요한 기능 강요 없이 유연한 설계 가능

### DIP (Dependency Inversion Principle, 의존성 역전 원칙)

DIP의 핵심 개념:

1. 상위 모듈(고수준 모듈, High-Level Module)은 하위 모듈(저수준 모듈, Low-Level Module)에 의존하면 안 된다.
   → 둘 다 추상화(Interface)에 의존해야 함
2. 추상화(Interface)는 구체적인 구현(Details)에 의존하면 안 된다.
   → 구체적인 구현이 추상화에 의존해야 함

#### DIP 위반 예제

[DIP 위반 예제](app/src/test/java/com/example/learningtest/solid/DIPViolated.kt)

DIP 위반 코드에서는
Customer 클래스가 구체적인 구현체(HumanLottoSeller, MachineLottoSeller)에 직접 의존 합니다.

* 문제점:
    * 새로운 판매자(OnlineLottoSeller) 가 추가되면 Customer 를 변경해야 함
    * OCP(개방-폐쇄 원칙)도 함께 위반됨

#### DIP 준수 (리팩토링)

[DIP 준수 (리팩토링)](app/src/test/java/com/example/learningtest/solid/DIPRefactored.kt)

* DIP 준수 코드에서는
    * LottoSeller 인터페이스를 도입하고
    * Customer 는 인터페이스만 참조하도록 변경

* 결과:
    * 상위 모듈이 하위 모듈에 직접 의존하지 않음
    * 새로운 판매자가 추가되어도 기존 코드 수정 불필요 (OCP도 준수)

SOLID 원칙을 따르면 코드가 확장 가능하고 유지보수가 쉬운 구조가 됩니다!

</details>


</details>
