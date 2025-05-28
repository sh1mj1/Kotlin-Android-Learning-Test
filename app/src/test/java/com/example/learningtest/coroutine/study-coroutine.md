## 스레드 기반 작업의 한계와 코루틴의 등장

1. JVM 상에서 실행되는 자바/코틀린 앱은 메인 스레드를 생성하고
   메인 스레드에서 코드를 실행한다.
2. 단일 스레드 앱은 한 번에 하나의 작업만 수행할 수 있다.
   복잡한 작업이나 네트워크 요청 등이 있으면 응답성이 떨어질 수 있다.
3. 멀티 스레드 프로그래밍을 사용하면 여러 작업을 동시에 실행할 수 있다.
   단일 스레드 프로그래밍에서의 문제를 해결할 수 있다.
4. 직접 `Thread` 클래스를 상속하여 스레드를 생성하고 관리할 수 있으나
   생성된 스레드의 재사용이 어려워 리소스를 낭비할 수 있다.
5. `Executor` 프레임워크를 사용하면 스레드 풀을 사용하여
   스레드 생성과 관리를 최적화하고 재사용을 용이하게 한다.
6. 스레드 블로킹은 스레드가 어떠한 작업의 완료를 기다리면서 리소스를 소비하지만  
   아무 일도 하지 않는 상태를 말한다.
7. 코루틴은 스레드 블로킹 문제를 해결하기 위해 등장했다.
   코루틴은 필요할 때 스레드 점유권을 양보하고 일시 중단하며
   다른 작업이 스레드를 사용할 수 있게 한다.
8. 일시 중단 후 재개된 코루틴은
   재개 시점에서 사용할 수 있는 스레드에 할당되어 실행된다.
9. 코루틴은 스레드에 비해서 생성과 전환 비용이 적고
   스레드에 자유롭게 붙었다가 떼질 수 있어서 작업을 할 수 있어 경량 스레드라고 불린다.
10. 코루틴을 사용하면 스레드 블로킹 없이 비동기적으로 작업을 처리할 수 있다.
    이를 통해 앱의 응답성을 향상시킬 수 있다.

## CoroutineDispatcher

1. `CoroutineDispatcher` 객체는 코루틴을 스레드로 보내 실행하는 객체이다.
   코루틴을 작업 대기열에 적재한 후 사용이 가능한 스레드로 보내 실행한다.
2. 제한된(Confined) Dispatcher는 코루틴을 실행하는 데 사용할 수 있는 스레드가
   특정 스레드 또는 스레드 풀로 제한된다.
   무제한(Unconfined) Dispatcher 는 코루틴을 실행하는데 사용할 수 있는 스레드가
   제한되지 않는다.
3. `newSingleThreadContext` 및 `newFixedThreadPoolContext` 함수를 사용해
   제한된 디스패처 객체를 생성할 수 있다.
4. `launch` 함수를 통해 코루틴을 실행할 때 `context` 인자로
   `CoroutineDispatcher` 객체를 넘기면
   해당 `CoroutineDispatcher` 객체를 사용해 코루틴이 실행된다.
5. 자식 코루틴은 기본적으로
   부모 코루틴의 `CoroutineDispatcher` 를 상속받아 사용한다.
6. 코루틴 라이브러리는 미리 정의된 `CoroutineDispatcher` 객체인
   `Dispatchers.Default`, `Dispatchers.IO`, `Dispatchers.Main` 을 제공한다.
7. `Dispatchers.IO` 는 입출력 작업을 위한 `CoroutineDispatcher` 객체로
   네트워크 요청이나 파일 I/O 등에 사용된다.
8. `Dispatchers.Default` 는 CPU 바운드 작업을 위한 `CoroutineDispatcher` 객체로
   대용량 데이터 처리 등을 하는 데 사용된다.
9. `limitedParallelism` 함수를 사용해 특정 연산을 위해 사용되는
   `Dispatchers.Default` 의 스레드 수를 제한할 수 있다.
10. `Dispatchers.IO` 와 `Dispatchers.Default` 는 코루틴 라이브러리에서 제공하는
    공유 스레드 풀을 사용한다.
11. `Dispatchers.Main` 은 메인 스레드에서 실행되어야 하는 작업에 사용되는
    `CoroutineDispatcher` 객체로 `Dispatchers.Main` 을 사용하기 위해서는
    별도의 라이브러리를 추가해야 한다.
12. `Dispatchers.Main` 은 일반적으로 UI 가 있는 앱에서 UI 를 업데이트하는데 사용된다.

## 코루틴 빌더와 Job

1. `runBlocking` 함수와 `launch` 함수는 코루틴을 만들기 위한 코루틴 빌더 함수이다.
2. `launch` 함수를 호출하면 `Job` 객체가 만들어져 반환되며,
   `Job` 객체는 코루틴의 상태를 추적하고 제어하는 데 사용된다.
3. `Job` 객체의 `join` 함수를 호출하면
   함수를 호출한 코루틴이 `Job` 객체의 실행이 완료될 때까지 일시 중단된다.
4. `joinAll` 함수를 사용해 복수의 코루틴이 실행 완료될때까지 대기할 수 있다.
5. `Job` 객체의 `cancel` 함수를 사용해 코루틴에 취소를 요청할 수 있다.
6. `Job` 객체의 `cancel` 함수가 호출되면 코루틴이 곧바로 취소되는 것이 아니라
   코루틴의 취소 플래그 상태가 바뀌고, 취소가 확인될 때 비로소 취소된다.
7. 코루틴에 취소를 요청한 후 취소가 완료될 때까지 대기하고 나서
   다음 코드를 실행하고 싶다면 `cancel` 대신 `cancelAndJoin` 함수를 사용하자.
8. `cancel` 함수를 호출하더라도
   코루틴이 취소를 확인할 수 없는 상태에서는 계속해서 실행될 수 있다.
9. `delay`, `yield` 함수나 `isActive` 프로퍼티 등을 사용하여
   코루틴이 취소를 확인할 수 있도록 만들 수 있다.
10. 코루틴은 생성(`New`), 실행 중(`Active`), 실행 완료 중(`Completing`),
    실행 완료(`Completed`), 취소 중(`Cancelling`), 취소 완료(`Cancelled`) 상태를 가진다.
11. `Job` 객체는 `isActive`, `isCancelled`, `isCompleted` 프로퍼티를 통해
    코루틴의 상태를 나타낸다.
12. `isActive` 는 생성(`New`) 상태일 때는 `false` 이고
    코루틴이 실행되면 `true` 로 바뀐다.
    코루틴에 `cancel` 함수를 통해 취소가 요청되거나 코루틴이 실행 완료되면
    다시 `false` 가 된다.
13. `isCancelled` 는 코루틴이 취소 중이거나 취소 완료되었을 때만 `true` 가 된다.
14. 취소 중(`Cancelling`) 상태는 코루틴에 취소가 요청되어
    `isCancelled` 가 `true` 인 상태이지만,
    아직 코루틴이 취소 완료되지 않고 동작 중인 상태이다.
15. `isCompleted` 는 코루틴이 취소 완료되거나 실행 완료되었을 때만 `true` 가 된다.
16. 자신 코루틴의 모든 코드를 실행했지만,
    자식 코루틴이 실행 완료되지 않았다면 실행 완료 중(`Completing`) 상태이다.
    이 때는 실행 중과 같은 상태값을 가진다.
17. 코루틴 라이브러리를 효율적으로 사용하기 위해서는 코루틴의 상태 변화를 이해하는 것이 중요하다.

| State      | isActive | isCompleted | isCancelled |
|------------|----------|-------------|-------------|
| New        | false    | false       | false       |
| Active     | true     | false       | false       |
| Completing | true     | false       | false       |
| Cancelling | false    | false       | true        |
| Cancelled  | false    | true        | true        |
| Completed  | false    | true        | false       |

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

## 구조화된 동시성

1. 구조화된 동시성의 원칙이란 비동기 작업을 구조화함으로써
   비동기 프로그래밍을 보다 안정적이고 예측적할 수 있게 만드는 것이다.
2. 코루틴은 구조화된 동시성의 원칙을 통해 코루틴을 부모-자식의 관계로 구조화한다.
   이로써, 안정적인 비동기 프로그래밍이 가능하게 한다.
3. 부모 코루틴은 자식 코루틴에게 실행 환경을 상속한다.
4. 코루틴 빌더 함수에 전달된 `CoroutineContext` 객체를 통해
   부모 코루틴의 실행 환경 일부 또는 전부를 덮어쓸 수 있다.
5. 코루틴 빌더가 호출될 때마다 코루틴 제어를 위한 새로운 `Job` 객체가 생성된다.
6. `Job` 객체는 부모 코루틴의 `Job`을 `parent` 프로퍼티를 통해 참조한다.
7. `parent` 프로퍼티가 `null` 일 경우 구조화의 시작점 역항를 하는 루트 `Job` 객체이다.
8. `Job` 객체는 자식 `Job` 객체들을 `Sequence<Job>` 타입의 `children` 프로퍼티를 통해 참조한다.
9. `Job` 객체는 코루틴의 구조화에 핵심적인 역할을 한다.
10. 부모 코루틴은 자식 코루틴이 완료될 때까지 완료되지 않는다.
    만약 부모 코루틴이 실행할 코드를 모두 실행했는데, 자식 코루틴이 완료되지 않았다면,
    부모 코루틴은 "실행 완료 중(`Completing`)" 상태를 가진다.
11. 부모 코루틴이 취소되면 취소가 모든 자식 코루틴으로 전파된다.
    하지만 자식 코루틴의 취소가 부모 코루틴으로 전파되지는 않는다.
12. `CoroutineScope` 객체를 사용해 코루틴의 실행 범위를 제어할 수 있다.
13. `CoroutineScope` 는 `CoroutineContext` 를 가진 인터페이스이다.
    코루틴 빌더 함수는 `CoroutineScope` 를 수신 객체로 가지는 확장 함수이다.
14. `launch` 나 `async` 가 호출되면, `CoroutineScope` 로부터
    실행 환경을 제공받아 코루틴이 실행된다.
15. `CoroutineScope` 객체에 대해 `cancel` 함수를 호출하여
    해당 범위에 있는 모든 코루틴을 취소시킬 수 있다.
16. `CoroutineScope` 객체에 대해 `cancel` 함수를 호출하는 것은
    그 스코프의 `CoroutineContext` 의 `Job` 객체에 대해 `cancel` 을 호출하는 것이다.
17. `CoroutineScope` 객체의 활성화 상태를
    `isActive` 확장 프로퍼티를 통해 확인할 수 있다.
18. `CoroutineScope` 객체에 대해 `isActive` 프로퍼티를 호출하는 것은
    그 스코프의 `CoroutineContext` 의 `Job` 객체에 대해 `isActive` 를 호출하는 것이다.
19. 별도의 범위를 가진 `CoroutineScope` 객체를 만들어서 코루틴의 구조화를 깰 수 있다.
20. `Job` 생성 함수를 호출하여 `Job` 객체를 생성할 수 있다.
    이를 통해 코루틴의 구조화를 깨거나 유지할 수 있다.
21. `Job` 생성 함수로 생성한 `Job` 객체는 자동으로 실행 완료되지 않으므로
    `complete` 함수를 호출하여 명시적으로 실행 완료 시켜야 한다.
22. `runBlocking` 함수는 호출한 스레드를 차단하는 반면에
    `launch` 함수는 호출한 스레드를 차단하지 않는다.
    `runBlocking` 으로 차단한 스레드는 해당 코루틴과 그 코루틴의 자식 코루틴만
    점유하여 사용할 수 있다.

## 예외 처리

1. 앱은 다양한 예외 상황에 노출되며,
   예외를 적절히 처리해 앱의 안정성을 확보할 수 있다.
2. 코루틴은 비동기 작업을 실행할 때 사용되기 때문에
   앱의 안정성을 위해 예외 처리가 필수적이다.
3. 코루틴에서 발생한 예외는 부모 코루틴으로 전파되며,
   적절히 처리되지 않으면 최상위 루트 코루틴까지 전파된다.
4. 예외를 전파받은 코루틴이 취소되면
   해당 코루틴의 모든 자식 코루틴에 취소가 전파된다.
5. 새로운 루트 `Job` 객체를 통해 코루틴의 구조화를 깨서
   코루틴의 예외 전파를 제한할 수 있다.
6. `SupervisorJob` 객체를 사용해 예외 전파를 제한할 수 있다.
   `SupervisorJob` 객체는 예외를 전파받지 않는 특수한 `Job` 객체이다.
7. `SupervisorJob` 객체는 예외를 전파받지 않지만, 예외 정보는 전달받는다.
8. 예외가 전파되거나 예외 정보가 전달된 경우
   해당 코루틴에서는 예외가 처리된 것으로 본다.
9. `CoroutineExceptionHandler` 는 공통 예외 처리기로서 동작한다.
   이미 처리된 예외에 대해서는 동작하지 않는다.
   즉, 예외가 마지막으로 전파되는 또는 전달되는 위치에
   `CoroutineExceptionhandler` 가 설정되지 않으면 동작하지 않는다.
10. `CoroutineExceptionHandler` 는 예외 전파를 제한하지 않는다.
11. 코루틴 내부에서 `try - catch` 문을 사용하여 예외를 처리할 수 있다.
12. 코루틴 빌더 함수에 대한 `try - catch` 문은 코루틴이 실행될 때
    발생하는 예외를 잡지 못한다.
13. `async` 함수로 생성된 코루틴에서 발생한 예외는 `await` 호출시 노출된다.
14. `async` 코루틴에서 발생한 예외 또한 부모 코루틴으로 전파된다.
    즉, `async` 로 만든 `Deferred` 객체에 대해 `await` 를 호출 할 때
    예외 처리도 해야 하고,
    부모 코루틴에도 `CoroutineExceptionHandler` 를 달아야 한다.
15. `CancellationException` 은 다른 예외와 달리 부코 코루틴으로 전달되지 않는다.
16. `CancellationException` 이 전파되지 않는 이유는
    `CancellationException` 은 코루틴을 취소하기 위한 특별한 예외이기 때문이다.
    `Job` 객체에 `cancel` 함수를 호출하면
    `CancellationException` 의 서브 클래스인 `JobCancellationException` 이
    발생하여 코루틴이 취소된다.
17. `withTimeOut` 함수를 사용해 코루틴의 실행 시간을 제한할 수 있다.
    `withTimeOut` 함수는 실행 시간 초과 시
    `CancellationException` 의 서브 클래스인 `TimeoutCancellationException` 이
    발생해 코루틴이 취소된다.
18. `withTimeOutOrNull` 을 사용하면 실행 시간 초과 시 `null` 이 반환되도록
    할 수 있다.

## 일시 중단 함수

1. 일시 중단 함수는 `suspend fun` 키워드로 선언되며
   일시 중단 지점이 포함된 코드를 재사용이 가능한 단위로 만들어 구조화하는 데 사용된다.
2. 일시 중단 함수는 코루틴이 아니다.
   일시 중단 지점을 포함할 수 있는 코드의 집합일 뿐이다.
3. 일시 중단 함수는 일시 중단이 가능한 지점에서만 호출할 수 있다.
   (코루틴이나 다른 일시 중단 함수 내부 등)
4. 일시 중단 함수 내부에서 `coroutineScope` 함수를 사용하여
   코루틴의 구조화를 깨지 않는 새로운 CoroutineScope 객체를 생성할 수 있다.
5. `coroutineScope` 함수를 사용하여 만든 `CoroutineScope` 객체를 사용하여
   `launch` 나 `async` 같은 코루틴 빌더를 호출할 수 있다.
   이를 사용하면 일시 중단 함수 내부에서 비동기 작업을 병렬로 실행할 수 있다.
6. `coroutineScope` 함수 대신 `supervisorScope`  함수를 사용하여
   일시 중단 함수 내부에서 생성된 코루틴의 예외 전파를 제한할 수 있다.

## 코루틴의 이해

1. 프로그래밍에서는 루틴을 "특정한 일을 처리하기 위한 일련의 명령" 이라는 뜻으로 사용하고 있다.
   이런 일련의 명령을 함수 또는 메서드라고 한다.
2. 서브루틴은 루틴의 하위에서 실행되는 루틴이다.
   즉, 함수 내부에서 호출되는 함수를 서브루틴이라고 한다.
3. 서브루틴은 한 번 실행되면 끝까지 실행된다.
   반면에 코루틴은 서로 간의 스레드 사용 권한을 양보하며 함께 실행된다.
4. `delay` 함수는 스레드를 양보하고 일정 시간동안 코루틴을 일시 중단시킨다.
5. `join` 과 `await` 함수를 호출한 코루틴은
   `join` 이나 `await` 의 대상이 된 코루틴의 작업이 완료될때까지 
   스레드를 양보하고 일시 중단한다.
6. `yield` 함수는 스레드 사용 권한을 명시적으로 양보하고자 할 때 사용한다.
7. 코루틴은 협력적으로 동작한다.
   코루틴은 스레드 사용 권한을 양보함으로써 스레드가 실제로 사용되지 않는 동안
   다른 코루틴이 스레드를 사용할 수 있도록 한다.
8. 코루틴이 스레드를 양보하면 코루틴은 일시 중단되며, 
   재개될 때 `CoroutineDispatcher` 객체를 통해 다시 스레드에 보내진다.
   `CoroutineDispatcher` 객체는 코루틴을 쉬고 있는 스레드 중 하나로 보낸다.
   그래서 코루틴은 일시 중단 전의 스레드와 다른 스레드에서 재개될 수 있다.
9. 코루틴이 스레드를 양보하지 않으면 실행 스레드가 바뀌지 않는다.
10. 코루틴 내부에서 `Thread.sleep` 함수를 사용하면 
    코루틴이 대기하는 시간동안 스레드를 양보하지 않고 블로킹한다.


   