# Data Class

Data classes in Kotlin are primarily used to hold data.  
For each data class, the compiler automatically generates additional member functions:

코틀린의 데이터 클래스는 주로 데이터를 보관하는데 사용된다.  
각 데이터 클래스에 대해 컴파일러는 자동으로 추가적인 멤버 함수를 생성한다:

- `equals()`
- `hashCode()`
- `toString()`
- `copy()`
- `componentN()`

check the [TestDataClass.kt](TestDataClass.kt) file for the example.

In Kotlin, data classes are designed to **promote immutability**, which is central to the kotlin's
philosophy of writing safe and reliable code.  
The **immutability** is achieved by declaring properties as `val`, making instances unchangeable
after creation.  
The `copy()` method complements this by allowing the **creation of new instances with modified
properties without altering the original object**.

코틀린에서 데이터 클래스는 안전하고 신뢰할 수 있는 코드를 작성하는 코틀린의 철학에 중요한 역할을 한다.  
**불변성을 촉진**하기 위해 데이터 클래스는 프로퍼티를 `val`로 선언하여 생성 후에는 변경할 수 없는 인스턴스를 만든다.  
`copy()` 메소드는 이를 보완하여 **원본 객체를 변경하지 않고 수정된 프로퍼티를 가진 새 인스턴스를 생성**할 수 있다.

This approach **prevents unintended side effects from mutable state**, enhances code safety, and
**aligns with functional programming principles** that Kotlin embraces.  
By facilitating immutable data handling and providing concise syntax for copying objects, Kotlin
enables developers to write clear, maintainable, and bug-resistant code.

이러한 접근은 **가변 상태로 인한 의도하지 않은 부작용을 방지**하고 코드 안전성을 향상시키며, **코틀린이 수용하는 함수형 프로그래밍 원칙과 일치**한다.  
불변 데이터 처리를 용이하게 하고 객체 복사에 대한 간결한 구문을 제공함으로써, 코틀린은 개발자가 명확하고 유지보수 가능하며 버그에 강한 코드를 작성할 수 있도록 한다.  
