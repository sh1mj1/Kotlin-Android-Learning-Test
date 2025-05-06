package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/*
코루틴은 작업 단위로서 코루틴이 스레드를 사용하지 않을 때 스레드 사용 권한을 양보하는 방식으로 스레드 사용을 최적화하고 스레드 blocking 을 방지한다.
코루틴 디스패처는 코루틴을 스레드로 보내서 실행시키는 역할.
코루틴 디스패처는 코루틴의 실행을 관리하는 주체로 자신에게 실행 요청된 코루틴들을 작업 대기열에 적재하고
자신이 사용할 수 있는 스레드가 새로운 작업을 실행할 수 있는 상태라면 스레드로 코루틴을 보내 실행될 수 있게 만든다.

* */

class BasicCoroutineTest : FreeSpec({

    "runBlocking, launch, async 는 코루틴을 생성하는 코루틴 빌더 함수이다" - {

        "runBlocking 은 현재 스레드를 블로킹한다" {
            val startTime = System.currentTimeMillis()
            runBlocking {
                delay(1.seconds)
            }
            val endTime = System.currentTimeMillis()
            val elapsed = endTime - startTime

            (elapsed >= 1.seconds.inWholeMilliseconds) shouldBe true
        }

        "launch 는 코루틴 내에서 코루틴을 생성하며 현재 스레드를 블로킹하지 않는다" {
            val startTime = System.currentTimeMillis()

            runBlocking {
                launch {
                    delay(1.seconds)
                }
                val elapsed = System.currentTimeMillis() - startTime

                (elapsed < 1.seconds.inWholeMilliseconds) shouldBe true
            }
        }

        "async 는 코루틴 내에서 코루틴을 생성하며 현재 스레드를 블로킹하지 않는다" {
            val startTime = System.currentTimeMillis()

            runBlocking {
                async {
                    delay(1.seconds)
                    "result"
                }
                val elapsed = System.currentTimeMillis() - startTime
                (elapsed < 1.seconds.inWholeMilliseconds) shouldBe true
            }
        }

        "launch 는 job 객체를 리턴하는 코루틴 빌더이다" {
            var job: Job? = null
            runBlocking {
                job =
                    launch {
                        1 + 1 // do some operation
                    }
            }
            job.shouldBeInstanceOf<Job>()
        }

        "job 은 join 함수로 코루틴이 종료될 때까지 기다릴 수 있다" {
            val result = mutableListOf<String>()

            runBlocking {
                val job1 =
                    launch {
                        delay(0.1.seconds)
                        result.add("job1")
                    }
                job1.join()

                val job2 =
                    launch {
                        delay(0.1.seconds)
                        result.add("job2")
                    }
                job2.join()

                result.add("done")

                result shouldBe listOf<String>("job1", "job2", "done")
            }
        }

        "async 는 Deferred 객체를 리턴하는 코루틴 빌더이다" {
            var deferred: Deferred<String>? = null

            runBlocking {
                deferred =
                    async {
                        (1 + 1).toString()
                    }
            }
            deferred.shouldBeInstanceOf<Deferred<String>>()
        }

        "Deferred 는 await 함수로 코루틴이 종료될 때까지 기다리며  결과를 가져올 수 있다" {
            var result = ""
            runBlocking {
                val deferred =
                    async {
                        delay(0.1.seconds)
                        "result"
                    }
                result = deferred.await()
                (result shouldBe "result").also {
                    println(it)
                }
            }
        }

        "CoroutineName 객체를 코루틴 빌더의 context 인자로 넘겨서 코루틴의 이름을 설정할 수 있다" {
            var runBlockingName: String? = null
            var launchName: String? = null
            var asyncName: String? = null

            runBlocking(context = CoroutineName("runBlockingName")) {
                runBlockingName = this.coroutineContext[CoroutineName]?.name
                launch(context = CoroutineName("launchName")) {
                    launchName = coroutineContext[CoroutineName]?.name
                }
                async(context = CoroutineName("asyncName")) {
                    asyncName = coroutineContext[CoroutineName]?.name
                }
            }
            runBlockingName shouldBe "runBlockingName"
            launchName shouldBe "launchName"
            asyncName shouldBe "asyncName"
        }
    }
})
