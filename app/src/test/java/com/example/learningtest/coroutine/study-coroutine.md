 ## `async` & `Deferred` & `withContext`

1. `async` 함수를 사용해서 코루틴을 실행하면 코루틴의 결과를 감싸는 `Deferred` 객체를 반환받는다.
2. `Deferred` 는 `Job`의 서브 타입으로 `Job` 객체에 결과값을 감싸는 기능이 추가된 객체이다.
3. `Deferred` 객체에 대해 `await` 함수를 호출하면 결과값을 반환받을 수 있다.
   `await` 함수를 호출한 코루틴은 `Deferred` 객체가 결과값을 반환받을 때까지 일시 중단 후 대기한다.
4. `awaitAll` 함수를 사용해 복수의 `Deferred` 코루틴의 결과값을 반환할 때까지 대기할 수 있다.
5. `awaitAll` 함수는 컬렉션에 대한 확장 함수로도 제공된다.
6. `withContext` 함수를 사용해서 `async`-`await` 쌍을 대체할 수 있다.
   물론 완전히 똑같이 동작하는 것은 아니다.
7. `withContext` 함수는 코루틴을 새로 생성하지 않는다.
   코루틴의 실행환경을 담는 `CoroutineContext` 만 변경해서
   코루틴을 실행하므로 이를 활용해 코루틴이 실행되는 스레드를 변경할 수 있다.
8. `withContext` 함수는 코루틴을 새로 생성하지 않는다.
   병렬로 실행되어야 하는 복수의 작업을 `withContext` 로 감싸 실행하면 순차 실행된다.
   이럴 때는 `withContext` 대신 `async` 를 사용해 작업이 병렬로 실행될 수 있도록 해야 한다.
9. `withContext` 로 인해 실행 환경이 변경되어 실행되는 코루틴은
   `withContext`의 작업을 모두 실행하면 다시 이전의 실행 환경으로 돌아온다.

## CoroutineContext

1. `CoroutineContext` 객체는 코루틴의 실행 환경을 설정하고 관리하는 객체이다.
   `CoroutineDispatcher`, `CoroutineName`, `Job`, `CoroutineExceptionHandler` 
   등의 주요 구성요소 객체를 조합해서 코루틴 실행환경을 정의한다.
   1. `CoroutineName`: 코루틴의 이름을 설정
   2. `CoroutineDispatcher`: 코루틴을 스레드로 보내 실행하는 객체
   3. `Job`: 코루틴을 조작하는 데 사용
   4. `CoroutineExceptionHandler`: 코루틴에서 발생한 예외를 처리하는 객체
2. `CoroutineContext` 객체는 키-값 쌍으로 구성 요소를 관리하며, 
   동일한 키에 대해 중복된 값을 허용하지 않는다.
   따라서 각 구성 요소를 한 개씩만 가질 수 있다.
3. 더하기(+) 연산을 사용해 `CoroutineContext` 의 구성요소를 조합할 수 있다.
4. 동일한 키를 가진 구성 요소가 여러 개 추가되면 
   나중에 추가된 구성 요소가 이전 값을 덮어씌운다. 
5. 구성요소의 동반 객체로 선언된 key 프로퍼티를 사용해 키 값에 접근할 수 있다.
6. 키를 연산자 함수인 `get`, 대괄호(`[]`) 과 함께 사용해서
   `CoroutineContext` 객체에 설정된 구성 요소에 접근할 수 있다.
7. `CoroutineName`, `CoroutineDispatcher`, `Job`, `CoroutineExceptionHandler` 는 
   동반 객체인 Key 를 통해 `CoroutineContext.Key` 를 구현하기 때문에 
   그 자체로 키로 사용할 수 있다.
   즉, `coroutineContext[CoroutineName]` 은
   `coroutineContext[CoroutineName.Key]` 와 같다
8. `CoroutineContext` 객체의 `minusKey` 함수를 사용하면 `CoroutineContext` 객체에서 
   특정 구성 요소를 제거한 뒤 객체를 반환받을 수 있다.
