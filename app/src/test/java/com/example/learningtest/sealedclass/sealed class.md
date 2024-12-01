# sealed class & interface

## What are them?

`Sealed classes & interfaces` provide **controlled inheritance of your class hierarchies**.  
All direct subclasses of a sealed class are known at compile time.  
No other subclasses may appear outside the module and package within which the sealed class is
defined.

sealed class & interface 는 클래스 계층 구조의 제어된 상속을 제공한다.  
sealed class 의 모든 직접 하위 클래스는 컴파일 시간에 알려진다.  
즉, 상위 클래스가 직접 하위 클래스에 대해 알고 있는 것이다.
다른 하위 클래스들은 상위 클래스가 알지 못한다.

## Purpose

Represents a restricted class hierarchy with a fixed set of subclasses.

고정된 하위 클래스 집합을 가진 제한된 클래스 계층 구조를 나타내기 위함.

* Characteristics:
    * All direct subclasses must be declared in the same file.
    * Enhances type safety with when expressions, as the compiler knows all possible subclasses.
    * Can have abstract members and can be extended by data classes or object declarations.

* 특징:
    * 모든 직접 하위 클래스는 동일한 파일에 선언되어야 한다.
    * 컴파일러는 모든 가능한 하위 클래스를 알기 때문에 `when` 표현식을 통해 타입 안전성을 향상시킨다.
    * 추상 멤버를 가질 수 있으며 데이터 클래스나 객체 선언으로 확장될 수 있다.

## used scenario

* Limited class inheritance is desired:
    * You have a predefined, finite set of subclasses that extend a class, all of which are known at
      compile time.
* Type-safe design is required:
    * Safety and pattern matching are crucial in your project. Particularly for state management or
      handling complex conditional logic. For an example, check out Use sealed classes with when
      expressions.
* Working with closed APIs:
    * You want robust and maintainable public APIs for libraries that ensure that third-party
      clients use the APIs as intended.

* 제한된 클래스 상속이 필요한 경우:
    * 컴파일 시간에 알려진 미리 정의된 유한 집합의 하위 클래스가 있는 클래스를 확장하고 싶은 경우.
* 타입 안전한 디자인이 필요한 경우:
    * 프로젝트에서 안전성과 패턴 매칭이 중요한 경우. 특히 상태 관리나 복잡한 조건 로직을 처리하는 경우.
      예를 들어, `when` 표현식을 사용한 sealed class 사용을 참조하십시오.
* 닫힌 API와 작업:
    * 제 3자 클라이언트가 의도된 대로 API를 사용하도록 보장하는 견고하고 유지 관리 가능한 라이브러리의
      공개 API가 필요한 경우.

### More concrete example for android

* [Use scenario for API request-response handling](TestSealedClassUserService.kt)
* [Use scenario for ui state handling](TestSealedClassUiState.kt)
* And also handling business logic like below

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

