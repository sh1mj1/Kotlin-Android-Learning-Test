package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class CoroutineStartTest : FreeSpec({

    /*
     * launch 함수가 호출되면 스레드를 사용하는 CoroutineDispatcher 객체에 launch 코루틴이 즉시 예약된다.
     * 하지만 rootJob 코루틴이 스레드를 양보하지 않고 계속해서 실행된다.
     * 그래서 launch 코루틴은 실행되지 못하며, rootJob 코루틴에 의해 로그에 2 가 추가되고 나서
     * 스레드가 자유로워져 launch 스레드가 실행된다.
     */
    "CoroutineStart.DEFAULT" - {
        "코루틴 빌더를 호출한 즉시 생성된 코루틴의 실행을 예약한다, 해당 코루틴 빌더 함수를 호출한 코루틴은 계속해서 실행된다." {
            val logs = mutableListOf<Int>()

            val rootJob =
                launch {
                    launch {
                        logs.add(1)
                    }
                    logs.add(2)
                }
            rootJob.join()

            logs shouldBe listOf(2, 1)
        }

        "일반적인 코루틴 시작 옵션은 실행되기 전에 취소되면 실행 대기 상태에서 실행되지 않고 종료된다" {
            val logs = mutableListOf<Int>()

            val rootJob =
                launch {
                    val childJob =
                        launch {
                            logs.add(1)
                        }
                    childJob.cancel()
                    logs.add(2)
                }
            rootJob.join()

            logs shouldBe listOf(2)
        }
    }

    "CoroutineStart.ATOMIC 은 실행 대기 상태에서 취소를 방지하기 위한 옵션이다." {
        val logs = mutableListOf<Int>()

        val rootJob =
            launch {
                val childJob =
                    launch(start = CoroutineStart.ATOMIC) {
                        logs.add(1)
                    }
                childJob.cancel()
                logs.add(2)
            }
        rootJob.join()

        logs shouldBe listOf(2, 1)
    }

    "CoroutineSTart.UNDISPATCHED" - {
        "CoroutineStart.UNDISPATCHED 가 적용된 코루틴은 CoroutineDispatcher 객체의 작업 대기열을 거치지 않고 곧바로 호출자의 스레드에 할당되어 실행된다" {
            val logs = mutableListOf<Int>()

            val rootJob =
                launch {
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        logs.add(1)
                    }
                    logs.add(2)
                }
            rootJob.join()

            logs shouldBe listOf(1, 2)
        }

        "코루틴 내부에서 일시 중단 후 재개될 때는 CoroutineDispatcher 객체를 거쳐 실행된다" {
            val logs = mutableListOf<Int>()

            val rootJob =
                launch {
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        logs.add(1)
                        yield()
                        logs.add(3)
                    }
                    logs.add(2)
                }
            rootJob.join()

            logs shouldBe listOf(1, 2, 3)
        }
    }
})
