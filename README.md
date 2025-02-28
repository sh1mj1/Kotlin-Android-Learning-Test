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