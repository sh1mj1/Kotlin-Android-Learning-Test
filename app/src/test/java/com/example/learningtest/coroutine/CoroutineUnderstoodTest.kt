package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.yield

@OptIn(DelicateCoroutinesApi::class)
class CoroutineUnderstoodTest : FreeSpec({

    /*
     * 1. 처음 스레드를 점유하는 것은 rootJob 코루틴
     *   이 코루틴은 launch 함수를 호출하여 launch 코루틴을 생성하지만
     *   launch 코루틴 생성 후에도 runBlocking 코루틴이 계속 스레드를 점유한다
     *   그래서 launch 코루틴은 실행 대기 상태에 머문다.
     *   rootJob 코루틴이 로그에 3을 추가하고 나서 `childJob.join()` 을 실행한 후에 스레드가 양보된다.
     * 2. 자유로워진 스레드에 launch 코루틴이 보내져 실행된다.
     *   로그에 1 을 추가하고 나서 delay 일시 중단 함수를 호출하여 스레드를 양보한다.
     *   하지만 rootJob 코루틴은 childJob.join() 에 의해 childJob 코루틴이 실행 완료될 때까지 재개되지 못하므로 실행되지 못한다.
     * 3. launch 코루틴은 delay 에 의한 일시 중단 시간 1초가 끝나고 재개되며 로그에 2 를 추가하고 실행이 완료된다.
     * 4. launch 코루틴 실행 완료 후, rootJob 코루틴이 재개되어 로그에 4 를 추가한다. */
    "join | await 가 호출되면 호출부의 코루틴은 스레드를 양보하고 일시 중단되며 join | await 의 대상이 된 코루틴은 실행 완료될 때까지 재개되지 않는다" {
        val logs = mutableListOf<Int>()
        val rootJob =
            launch {
                val childJob =
                    launch {
                        logs.add(1)
                        delay(100L)
                        logs.add(2)
                    }
                logs.add(3)
                childJob.join()
                logs.add(4)
            }
        rootJob.join()

        // 항상 이 순서로
        logs.toList() shouldBe listOf(3, 1, 2, 4)
    }

    /*
     * "childJob working" 이 무한히 출력된다.
     * rootJob 코루틴이 delay 일시 중단 함수를 호출하여 메인 스레드를 양보하면
     * launch 코루틴이 메인 스레드를 점유하고 양보하지 않기 때문이다.
     * */
    "rootJob 이 delay 로 childJob 이 스레드를 점유한 상태에서 childJob.cancel() 이 호출될 수 없어서 무한 루프를 돈다 ".config(
        enabled = false,
    ) {
        var rootJobCompletedWell = false
        val rootJob =
            launch {
                val childJob =
                    launch {
                        while (this.isActive) {
                            println("childJob working")
                        }
                    }
                delay(100L)
                childJob.cancel()
                rootJobCompletedWell = true
            }
        rootJob.join()

        rootJobCompletedWell shouldBe false
    }

    "rootJob 이 delay 로 childJob 이 스레드를 점유한 상태에서 yield 를 통해 스레드를 양보하는 지점이 생겨서 childJob.cancel() 이 호출되어 루프를 탈출할 수 있다" {
        var rootJobCompletedWell = false
        val rootJob =
            launch {
                val childJob =
                    launch {
                        while (this.isActive) {
                            println("childJob working")
                            yield()
                        }
                    }
                delay(100L)
                childJob.cancel()
                rootJobCompletedWell = true
            }
        rootJob.join()
        rootJobCompletedWell shouldBe true
    }

    /*
     * co1 은 MyThread-1 에서 실행될 때도 있고, MyThread-2 에서 실행될 때도 있다.
     * co1 의 실행 스레드가 바뀌는 이유는 co1 이 재개될 때 CoroutineDispatcher 객체가 자신이 사용할 수 있는 스레드 중 하나에 co1 을 보낸다.
     * 코루틴 실행 스레드는 코루틴의 실행이 재개될 때만 바뀔 수 있다.
     * */
    "실행 스레드가 바뀌는 모습 관측" {
        val dispatcher = newFixedThreadPoolContext(2, "MyThread")

        launch(dispatcher + CoroutineName("co1")) {
            repeat(5) {
                println("[${Thread.currentThread().name}, [${coroutineContext[CoroutineName]?.name}]] 코루틴 실행이 일시중단됨")
                delay(100L)

                println("[${Thread.currentThread().name}] 코루틴 실행이 재개됨")
            }
        }
    }
})
