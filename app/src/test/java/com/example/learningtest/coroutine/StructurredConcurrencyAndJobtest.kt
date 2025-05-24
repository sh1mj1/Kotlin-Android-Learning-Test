package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StructurredConcurrencyAndJobtest : FreeSpec({
    "구조화 깨기 새로운 CoroutineScope 를 생성하여 Job 계층 구조를 벗어나면 부모 Job 이 완료되어도 자식 Coroutine 은 완료되지 않는다" {
        var coroutineIsCompletedWell1 = false
        var coroutineIsCompletedWell2 = false
        var coroutineIsCompletedWell3 = false
        var coroutineIsCompletedWell4 = false
        var coroutineIsCompletedWell5 = false

        val rootJob =
            launch {
                val newScope = CoroutineScope(Dispatchers.IO)
                newScope.launch(CoroutineName("1")) {
                    launch(CoroutineName("3")) {
                        delay(100L)
                        coroutineIsCompletedWell3 = true
                    }
                    launch(CoroutineName("4")) {
                        delay(100L)
                        coroutineIsCompletedWell4 = true
                    }
                    delay(100L)
                    coroutineIsCompletedWell1 = true
                }
                newScope.launch(CoroutineName("2")) {
                    launch(CoroutineName("5")) {
                        delay(100L)
                        coroutineIsCompletedWell5 = true
                    }
                    delay(100L)
                    coroutineIsCompletedWell2 = true
                }
            }

        rootJob.join()
        coroutineIsCompletedWell1 shouldBe false
        coroutineIsCompletedWell2 shouldBe false
        coroutineIsCompletedWell3 shouldBe false
        coroutineIsCompletedWell4 shouldBe false
        coroutineIsCompletedWell5 shouldBe false
    }

    "구조화 깨기 launch 시 새로운 Job 객체를 인자로 넘겨주면 부모 Job 이 완료되어도 자식 Coroutine 은 완료되지 않는다" {
        var coroutineIsCompletedWell1 = false
        var coroutineIsCompletedWell2 = false
        var coroutineIsCompletedWell3 = false
        var coroutineIsCompletedWell4 = false
        var coroutineIsCompletedWell5 = false

        val rootJob =
            launch {
                val newRootJob = Job()
                launch(CoroutineName("1") + newRootJob) {
                    launch(CoroutineName("3")) {
                        delay(500L)
                        coroutineIsCompletedWell3 = true
                    }
                    launch(CoroutineName("4")) {
                        delay(500L)
                        coroutineIsCompletedWell4 = true
                    }
                    delay(500L)
                    coroutineIsCompletedWell1 = true
                }

                launch(CoroutineName("2") + newRootJob) {
                    launch(CoroutineName("5")) {
                        delay(500L)
                        coroutineIsCompletedWell5 = true
                    }
                    delay(500L)
                    coroutineIsCompletedWell2 = true
                }
            }
        rootJob.join()

        coroutineIsCompletedWell1 shouldBe false
        coroutineIsCompletedWell2 shouldBe false
        coroutineIsCompletedWell3 shouldBe false
        coroutineIsCompletedWell4 shouldBe false
        coroutineIsCompletedWell5 shouldBe false
    }

    "Job 사용해 일부 코루틴만 취소되지 않도록 하기 - newRootJob 에 포함되지 않은 Job(5)은 취소되지 않는다." {
        var coroutineIsCompletedWell1 = false
        var coroutineIsCompletedWell2 = false
        var coroutineIsCompletedWell3 = false
        var coroutineIsCompletedWell4 = false
        var coroutineIsCompletedWell5 = false

        val rootJob =
            launch {
                val newRootJob = Job()

                launch(CoroutineName("1") + newRootJob) {
                    launch(CoroutineName("3")) {
                        delay(100L)
                        coroutineIsCompletedWell3 = true
                    }
                    launch(CoroutineName("4")) {
                        delay(100L)
                        coroutineIsCompletedWell4 = true
                    }
                    delay(100L)
                    coroutineIsCompletedWell1 = true
                }
                launch(CoroutineName("2") + newRootJob) {
                    launch(CoroutineName("5") + Job()) {
                        delay(100L)
                        coroutineIsCompletedWell5 = true
                    }
                    delay(100L)
                    coroutineIsCompletedWell2 = true
                }
                delay(100L)
                newRootJob.cancel()
            }
        rootJob.join()
        delay(1000L)

        coroutineIsCompletedWell1 shouldBe false
        coroutineIsCompletedWell2 shouldBe false
        coroutineIsCompletedWell3 shouldBe false
        coroutineIsCompletedWell4 shouldBe false
        coroutineIsCompletedWell5 shouldBe true
    }

    "생성된 Job 의 부모를 명시적으로 설정하여 구조화된 동시성을 유지할 수 있다" {
        var coroutineIsCompletedWell2 = false
        launch(CoroutineName("1")) {
            val newJob = Job(parent = coroutineContext[Job])
            val childJob =
                launch(CoroutineName("2") + newJob) {
                    delay(500L)
                    coroutineIsCompletedWell2 = true
                }
            childJob.invokeOnCompletion {
                newJob.complete()
            }
        }
        delay(1000L)
        coroutineIsCompletedWell2 shouldBe true
        // 하지만 이렇게 Job 객체를 생성할 경우 문제가 생길 수 있다.
        // newJob 은 자동으로 실행 완료 처리되지 않기 때문에 명시적으로 complete 함수를 호출해주어야 한다.
    }
})
