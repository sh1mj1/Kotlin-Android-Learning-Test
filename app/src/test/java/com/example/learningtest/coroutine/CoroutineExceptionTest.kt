package com.example.learningtest.coroutine

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineExceptionTest : FreeSpec({

    /*
     * 이 테스트 코드로는 예외를 shouldThrow 로 잡을 수도 없다.
     * */
    "자식 코루틴에서 예외가 발생하면 부모 코루틴과 형제 코루틴이 모두 취소된다 ".config(enabled = false) {
        var coroutine1CompletedWell = false
        var coroutine2CompletedWell = false

        shouldThrow<Exception> {
            val rootJob =
                launch {
                    launch(CoroutineName("1")) {
                        launch(CoroutineName("3")) {
                            throw Exception("exception")
                        }
                        delay(100L)
                        coroutine1CompletedWell = true
                    }
                    launch(CoroutineName("2")) {
                        delay(100L)
                        coroutine2CompletedWell = true
                    }
                    delay(500L)
                }
            rootJob.join()
        }

        coroutine1CompletedWell shouldBe false
        coroutine2CompletedWell shouldBe false
    }

    /*
     * 여전히 테스트 결과에서는 예외가 터졌다는 메시지는 볼 수 있다
     * */
    "자식 코루틴에서 예외가 발생해도 Job 객체를 사용하여 구조화를 깨면 부모 코루틴과 형제 코루틴은 취소되지 않는다" {
        var coroutine1CompletedWell = false
        var coroutine2CompletedWell = false

        val rootJob =
            launch {
                launch(CoroutineName("1") + Job()) {
                    launch(CoroutineName("3")) {
                        throw Exception("exception")
                    }
                    delay(100L)
                    coroutine1CompletedWell = true
                }
                launch(CoroutineName("2")) {
                    delay(100L)
                    coroutine2CompletedWell = true
                }
                delay(500L)
            }
        rootJob.join()

        coroutine1CompletedWell shouldBe false
        coroutine2CompletedWell shouldBe true
    }

    "구조화가 깨지면 큰 작업이 취소되더라도 작은 작업이 취소되지 않아서 비동기 작업을 불안정하게 한다" {
        var coroutine1CompletedWell = false
        var coroutine2CompletedWell = false
        var coroutine3CompletedWell = false

        val rootJob =
            launch {
                launch(CoroutineName("1") + Job()) {
                    launch(CoroutineName("3")) {
                        delay(100L)
                        coroutine3CompletedWell = true
                    }
                    delay(100L)
                    coroutine1CompletedWell = true
                }
                launch(CoroutineName("2")) {
                    delay(100L)
                    coroutine2CompletedWell = true
                }
                delay(300L)
            }
        delay(100L) // 모든 코루틴들이 생성될 때까지 대기
        rootJob.cancelAndJoin()
        delay(600L)

        coroutine1CompletedWell shouldBe true
        coroutine2CompletedWell shouldBe false
        coroutine3CompletedWell shouldBe true
    }

    /*
     * 하지만 SupervisorJob 객체가 rootJob 과의 구조화를 깬다
     * */
    "SupervisorJob 객체를 사용하여 자식 코루틴에서 예외가 발생해도 다른 형제 코루틴은 취소되지 않는다" {
        var rootcoroutineCompletedWell = false
        var coroutine1CompletedWell = false
        var coroutine2CompletedWell = false

        val supervisorJob = SupervisorJob()

        val rootJob =
            launch {
                launch(CoroutineName("1") + supervisorJob) {
                    launch(CoroutineName("3")) {
                        throw Exception("exception")
                    }
                    delay(100L)
                    coroutine1CompletedWell = true
                }
                launch(CoroutineName("2") + supervisorJob) {
                    delay(100L)
                    coroutine2CompletedWell = true
                }
                delay(300L)
                rootcoroutineCompletedWell = true
            }
        delay(100L) // 모든 코루틴들이 생성될 때까지 대기
        rootJob.join()
        delay(600L)

        rootcoroutineCompletedWell shouldBe true
        coroutine1CompletedWell shouldBe false
        coroutine2CompletedWell shouldBe true
        supervisorJob.parent shouldNotBe rootJob
        // supervisorJob 까지는 예외가 전파되지 않아서 코루틴 1 까지만 예외가 전파된다.
    }

    "SupervisorJob를 사용하여 코루틴 구조를 유지하면서 예외 처리하기" - {
        "SupervisorJob의 자식 코루틴에서 발생한 예외는 다른 자식 코루틴과 부모 코루틴에 전파되지 않는다 (구조화 유지)" {
            var rootCoroutineCompletedWell = false
            var coroutine1CompletedWell = false
            var coroutine2CompletedWell = false

            val rootJob =
                launch {
                    val supervisorJob = SupervisorJob(parent = coroutineContext.job)

                    launch(CoroutineName("1") + supervisorJob) {
                        launch(CoroutineName("3")) {
                            throw Exception("exception")
                        }
                        delay(100L)
                        coroutine1CompletedWell = true
                    }
                    launch(CoroutineName("2") + supervisorJob) {
                        delay(100L)
                        coroutine2CompletedWell = true
                    }
                    delay(300L)
                    rootCoroutineCompletedWell = true
                    supervisorJob.complete()
                }
            delay(100L) // 모든 코루틴들이 생성될 때까지 대기
            rootJob.join()

            delay(600L)

            rootCoroutineCompletedWell shouldBe true
            coroutine1CompletedWell shouldBe false
            coroutine2CompletedWell shouldBe true
            // 이렇게 되면 supervisorJob 은 구조화를 깨지 않는다.
        }

        "CoroutineScope와 SupervisorJob을 함께 사용하여 예외 처리하기 (구조화 유지)" {
            var rootCoroutineCompletedWell = false
            var coroutine1CompletedWell = false
            var coroutine2CompletedWell = false

            val rootJob =
                launch {
                    CoroutineScope(SupervisorJob()).apply {
                        launch(CoroutineName("1")) {
                            launch(CoroutineName("3")) {
                                throw Exception("exception")
                            }
                            delay(100L)
                            coroutine1CompletedWell = true
                        }
                        launch(CoroutineName("2")) {
                            delay(100L)
                            coroutine2CompletedWell = true
                        }
                    }
                    delay(100L)
                    rootCoroutineCompletedWell = true
                }
            delay(100L) // 모든 코루틴들이 생성될 때까지 대기
            rootJob.join()
            delay(600L)

            rootCoroutineCompletedWell shouldBe true
            coroutine1CompletedWell shouldBe false
            coroutine2CompletedWell shouldBe true
        }

        "supervisorScope 함수를 사용하여 구조화도 깨지 않으면서 자식 코루틴의 예외 전파도 제한할 수 있다" {
            var coroutine1CompletedWell = false
            var coroutine2CompletedWell = false
            var coroutine3CompletedWell = false
            var supervisorCoroutineCompletedWell = false

            val rootJob =
                launch {
                    supervisorScope {
                        launch(CoroutineName("1")) {
                            launch(CoroutineName("3")) {
                                throw Exception("exception")
                            }
                            delay(100L)
                            coroutine1CompletedWell = true
                        }
                        launch(CoroutineName("2")) {
                            delay(100L)
                            coroutine2CompletedWell = true
                        }
                        delay(100L)
                        supervisorCoroutineCompletedWell = true
                    }
                }
            delay(100L) // 모든 코루틴들이 생성될 때까지 대기
            rootJob.join()
            delay(600L)

            coroutine1CompletedWell shouldBe false
            coroutine2CompletedWell shouldBe true
            coroutine3CompletedWell shouldBe false
            supervisorCoroutineCompletedWell shouldBe true
        }
    }
})
