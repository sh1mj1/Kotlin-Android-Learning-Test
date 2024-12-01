# Kotest

In Kotest a test is essentially just a function `TestContext -> Unit`.
That function contains your test logic.

Any assert statements invoked in this function that throw an exception will be intercepted by the
framework and used to mark that test as failed or success.

Test functions are not defined manually.  
But, Kotest DSL provides several ways in which these functions can be created and nested.  
The DSL is accessed by creating a class that extends from a class that implements a particular
testing style.

Kotest 는 테스트가 `TestContext -> Unit` 함수로 정의된다.  
이 함수가 테스트 로직을 포함한다.

이 함수에서 발생하는 모든 assert 문은 예외를 던지고, 프레임워크가 이를 가로채서 테스트가 실패했는지 성공했는지를 표시한다.

테스트 함수는 수동으로 정의되지 않는다.
대신에, Kotest DSL 은 이러한 함수가 생성되고 중첩될 수 있는 여러 가지 방법을 제공한다.
DSL 은 특정 테스팅 스타일을 구현하는 클래스를 상속하는 클래스를 생성하여 액세스된다.

Test Style

* [Fun Spec](KotestFunSpec.kt)
* [Describe Spec](KotestDescribeSpec.kt)
* [String Spec](KotestStringSpec.kt)
* [Free Spec](KotestFreeSpec.kt)
* [Word Spec](KotestWordSpec.kt)
* [Feature Spec](KotestFeatureSpec.kt)
* [Expect Spec](KotestExpectSpec.kt)
* [Annotation Spec](KotestAnnotationSpec.kt)

