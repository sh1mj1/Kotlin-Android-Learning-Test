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


