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
