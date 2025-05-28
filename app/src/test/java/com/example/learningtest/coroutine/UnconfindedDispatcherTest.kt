package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class UnconfindedDispatcherTest : FreeSpec({

    "UnconfindedDispatcher 는 코루틴이 자신을 실행시킨 스레드에서 즉시 실행된다" {
        val myDispatcher = newFixedThreadPoolContext(2, "Mine")
        var rootJobThreadName = ""
        var childJobThreadName = ""
        val rootJob =
            launch(myDispatcher) {
                rootJobThreadName = Thread.currentThread().name.split(" ")[0]
                launch(Dispatchers.Unconfined) {
                    childJobThreadName = Thread.currentThread().name.split(" ")[0]
                }
            }
        rootJob.join()
        rootJobThreadName shouldBe childJobThreadName
    }

    "UnconfinedDispatcher 를 사용하지 않으면 코루틴은 다른 스레드에서 실행될 수 있다".config(enabled = false) {
        val myDispatcher = newFixedThreadPoolContext(2, "Mine")
        var rootJobThreadName = ""
        var childJobThreadName = ""
        val rootJob =
            launch(myDispatcher) {
                rootJobThreadName = Thread.currentThread().name.split(" ")[0]
                launch {
                    childJobThreadName = Thread.currentThread().name.split(" ")[0]
                }
            }
        rootJob.join()

        // 같은 스레드에서 실행될 가능성도 있음.
        rootJobThreadName shouldNotBe childJobThreadName
    }

    "UnconfinedDispatcher 를 사용해 실행된 코루틴이 일시 중단 후 재개될 때 어떻게 동작하는지?" {
        val logs = mutableListOf<Int>()

        val rootJob =
            launch {
                logs.add(1)
                launch(Dispatchers.Unconfined) {
                    logs.add(2)
                }
                logs.add(3)
            }
        rootJob.join()
        logs shouldBe listOf(1, 2, 3)
    }

    "UnconfinedDispatcher 를 사용하지 않고 코루틴을 실행하면 자식 코루틴은 부모 코루틴의 작업이 완료된 후 실행된다" {
        val logs = mutableListOf<Int>()

        val rootJob =
            launch {
                logs.add(1)
                launch {
                    logs.add(2)
                }
                logs.add(3)
            }
        rootJob.join()
        logs shouldBe listOf(1, 3, 2)
    }

    /*
     * DefaultExecutor 스레드는 delay 함수를 실행하는 스레드이며,
     * delay 함수가 일시 중단을 종료하고 코루틴을 재개할 때 사용하는 스레드이다
     * 즉, 재개 이후의 launch 코루틴은 자신을 재개시킨 스레드인 DefaultExecutor 를 사용하게 된다
     * 어떤 스레드가 코루틴을 재개시키는지 예측하기 어렵기 때문에 일반적인 상황에서 UnconfinedDispatcher 를 사용하는 것은 좋지 않다
     * */
    "UnconfinedDispatcher 를 사용해 실행되는 코루틴은 일시 중단 이후에는 자신을 재개시킨 스레드에서 동작한다" {
        val myDispatcher = newFixedThreadPoolContext(1, "mine")
        val rootJob =
            launch(myDispatcher) {
                launch(Dispatchers.Unconfined) {
                    Thread.currentThread().name shouldContain "mine"
                    delay(100L)
                    Thread.currentThread().name shouldContain "DefaultExecutor"
                }
            }
        rootJob.join()
    }

    "UnconfindDispatcher 로 실행되는 코루틴은 재개될 때 자신을 재개시킨 스레드를 사용하고 " +
        "CoroutinStart.UNDISPATCHED 옵션이 적용된 코루틴은 자신이 실행되는 CoroutineDispatcher 객체를 사용하여 재개된다" {
            val myDispatcher = newFixedThreadPoolContext(2, "mine")
            val rootJob =
                launch(myDispatcher) {
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        Thread.currentThread().name shouldContain "mine"
                        delay(100L)
                        Thread.currentThread().name shouldContain "mine"
                    }.join()

                    launch(Dispatchers.Unconfined) {
                        Thread.currentThread().name shouldContain "mine"
                        delay(100L)
                        Thread.currentThread().name shouldContain "DefaultExecutor"
                    }
                }
            rootJob.join()
        }
})
