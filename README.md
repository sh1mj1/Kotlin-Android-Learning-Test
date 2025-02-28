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

</details>