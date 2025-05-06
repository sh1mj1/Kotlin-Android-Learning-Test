package com.example.learningtest.coroutine

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlin.time.Duration.Companion.seconds

class CoroutineBuilderJobTest : FreeSpec({
    "순차 실행이 필요한 작업을 launch 로 단순 병렬 실행하면 의도한 순서를 보장할 수 없다" {
        val logs = mutableListOf<String>()

        runBlocking {
            launch(Dispatchers.IO) {
                logs.add("토큰 업데이트 시작")
                delay(100)
                logs.add("토큰 업데이트 완료")
            }

            launch(Dispatchers.IO) {
                logs.add("네트워크 요청")
            }
        }

        logs shouldContainExactlyInAnyOrder listOf("토큰 업데이트 시작", "네트워크 요청", "토큰 업데이트 완료")
        logs.indexOf("네트워크 요청") shouldBeLessThan logs.indexOf("토큰 업데이트 완료")
    }

    "Job 간의 의존 관계가 있을 때는 join 으로 순차 실행을 보장할 수 있다" {
        val logs = mutableListOf<String>()

        runBlocking {
            val updateTokenJob =
                launch(Dispatchers.IO) {
                    logs.add("토큰 업데이트 시작")
                    delay(100)
                    logs.add("토큰 업데이트 완료")
                }

            updateTokenJob.join()

            launch(Dispatchers.IO) {
                logs.add("네트워크 요청")
            }.join()
        }

        logs shouldBe
            listOf(
                "토큰 업데이트 시작",
                "토큰 업데이트 완료",
                "네트워크 요청",
            )
    }

    "여러 Job 이 완료된 후 다음 작업을 실행하려면 joinAll 로 순서를 보장할 수 있다" {
        val logs = mutableListOf<String>()

        runBlocking {
            val convertImageJob1 =
                launch(Dispatchers.Default) {
                    delay(10)
                    logs.add("이미지1 변환 완료")
                }

            // convertImageJob1 과 convertImageJob2 사이에 convertImageJob1.join() 이 없으므로 동시에 실행되는 시점이 존재.
            val convertImageJob2 =
                launch(Dispatchers.Default) {
                    delay(10)
                    logs.add("이미지2 변환 완료")
                }

            joinAll(convertImageJob1, convertImageJob2)

            launch(Dispatchers.IO) {
                logs.add("이미지1,2 업로드")
            }.join()
        }

        (
            logs == listOf("이미지1 변환 완료", "이미지2 변환 완료", "이미지1,2 업로드") || logs ==
                listOf(
                    "이미지2 변환 완료",
                    "이미지1 변환 완료",
                    "이미지1,2 업로드",
                )
        ) shouldBe true
    }

    "Job.cancel() 을 호출하면 코루틴이 중단된다" {
        val logs = mutableListOf<String>()
        val startTime = System.currentTimeMillis()

        runBlocking {
            val job =
                launch(Dispatchers.Default) {
                    repeat(10) { i ->
                        delay(1000)
                        logs.add("[$i] ${System.currentTimeMillis() - startTime}ms")
                    }
                }

            delay(100)
            job.cancel()
        }

        logs.shouldBeEmpty()
    }

    "양보 지점이 없는 무한 루프 코루틴은 cancel 되어도 종료되지 않는다" {
        val startTime = System.currentTimeMillis()
        shouldThrow<IllegalStateException> {
            runBlocking {
                val job =
                    launch(Dispatchers.Default) {
                        while (true) {
                            val currentTime = System.currentTimeMillis()
                            val elapsed = currentTime - startTime
                            check(elapsed < 500)
                        }
                    }

                delay(100)
                job.cancel()
            }
        }
    }

    "delay, yield, CoroutineScope.isActive 를 사용하여 양보 지점 추가해서 취소 가능하게 만들 수 있다" - {
        "delay 를 사용하여 취소 가능하게 만들기" {
            val startTime = System.currentTimeMillis()
            shouldNotThrow<IllegalStateException> {
                runBlocking {
                    val job =
                        launch(Dispatchers.Default) {
                            withTimeout(2.seconds) {
                                while (true) {
                                    delay(10)

                                    val currentTime = System.currentTimeMillis()
                                    val elapsed = currentTime - startTime
                                    check(elapsed < 500)
                                }
                            }
                        }
                    job.cancel()
                }
            }
        }

        "yield 를 사용하여 취소 가능하게 만들기" {
            val startTime = System.currentTimeMillis()
            shouldNotThrow<IllegalStateException> {
                runBlocking {
                    val job =
                        launch(Dispatchers.Default) {
                            withTimeout(2.seconds) {
                                while (true) {
                                    yield()

                                    val currentTime = System.currentTimeMillis()
                                    val elapsed = currentTime - startTime
                                    check(elapsed < 500)
                                }
                            }
                        }
                    job.cancel()
                }
            }
        }

        "CoroutineScope.isActive 를 사용하여 취소 가능하게 만들기" {
            val startTime = System.currentTimeMillis()
            runBlocking {
                shouldNotThrow<IllegalStateException> {
                    val job =
                        launch(Dispatchers.Default) {
                            withTimeout(2.seconds) {
                                while (true) {
                                    while (isActive) {
                                        val currentTime = System.currentTimeMillis()
                                        val elapsed = currentTime - startTime
                                        check(elapsed < 500)
                                    }
                                }
                            }
                        }
                    job.cancel()
                }
            }
        }
    }

    /*
     * cancel 호출 직후 작업을 요청했을 때, job 이 바로 취소 완료되지 않는다.
     * 취소 완료되지 않은 job 때문에 이후 작업이 먼저 실행될 수도 있다.
     * TODO() cancel 호출 직후 작업을 요청했을 때, 취소 완료되지 않은 job 때문에 이후 작업이 먼저 실행되는 테스트 케이스
     * */
    "Job.cancelAndJoin()을 사용하면 취소 후 순차 처리를 보장할 수 있다" {
        val logs = mutableListOf<String>()

        runBlocking {
            val job =
                launch(Dispatchers.Default) {
                    repeat(10) {
                        delay(10)
                        logs.add("작업 #$it")
                    }
                }
            job.cancelAndJoin()
            logs.add("취소 후 동작 실행")
        }
        logs shouldHaveSize (1)
        logs.first() shouldBe "취소 후 동작 실행"
    }

    "즉시 시작되는 launch 코루틴은 launch 호출과 동시에 실행된다" {
        val logs = mutableListOf<String>()

        runBlocking {
            val startTime = System.currentTimeMillis()

            val immediateJob =
                launch {
                    logs.add(elapsedTimeMilli(startTime).toString())
                } // == launch(start = CoroutineStart.DEFAULT) { ... }
            delay(100)
            immediateJob.join()
        }

        val time = logs.first().toLong()
        time shouldBeLessThan 100L
    }

    "CoroutineStart.LAZY 를 사용하면 코루틴이 생성되어도 실행되지 않는다" {
        val logs = mutableListOf<String>()

        runBlocking {
            val startTime = System.currentTimeMillis()

            val lazyJob =
                launch(start = CoroutineStart.LAZY) {
                    logs.add(elapsedTimeMilli(startTime).toString())
                }

            delay(100)
            logs.shouldBeEmpty()

            lazyJob.start()
            lazyJob.join()
        }

        val time = logs.first().toLong()
        time shouldBeGreaterThanOrEqual 100L
    }
})

fun elapsedTimeMilli(startTime: Long): Long = System.currentTimeMillis() - startTime
