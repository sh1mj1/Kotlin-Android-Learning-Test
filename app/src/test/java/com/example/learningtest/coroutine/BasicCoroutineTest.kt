package com.example.learningtest.coroutine

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/*
* 코루틴은 작업 단위로서 코루틴이 스레드를 사용하지 않을 때 스레드 사용 권한을 양보하는 방식으로 스레드 사용을 최적화하고 스레드 blocking 을 방지한다.
* 코루틴 디스패처는 코루틴을 스레드로 보내서 실행시키는 역할.
* 코루틴 디스패처는 코루틴의 실행을 관리하는 주체로 자신에게 실행 요청된 코루틴들을 작업 대기열에 적재하고
* 자신이 사용할 수 있는 스레드가 새로운 작업을 실행할 수 있는 상태라면 스레드로 코루틴을 보내 실행될 수 있게 만든다.
* */
@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
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

        "코루틴은 구조화를 제공해 코루틴 내부에서 새로운 코루틴을 실행할 수 있다." {
            shouldNotThrow<Exception> {
                runBlocking {
                    runBlocking { 1 + 1 }
                    1 + 1
                }
            }
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

        "async 는 Job 의 하위 인터페이스인 Deferred 타입 객체를 리턴하는 코루틴 빌더이다" {
            var deferred: Deferred<String>? = null

            runBlocking {
                deferred =
                    async {
                        (1 + 1).toString()
                    }
            }

            deferred.shouldBeInstanceOf<Deferred<String>>()
            deferred.shouldBeInstanceOf<Job>()
        }

        "Deferred 는 await 함수로 코루틴이 종료될 때까지 기다리며  결과를 가져올 수 있다" {
            var result: String
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

    "CoroutineDispatcher 는 코루틴을 스레드로 보내서 실행시키거나 대기열에 적재하는 코루틴 관리자이다" - {

        "newFixedThreadPoolContext 로 만든 디스패처는 ExecutorService 기반이며 데몬 스레드를 사용한다" {
            val isDaemonFlags = mutableSetOf<Boolean>()

            val dispatcher = newFixedThreadPoolContext(2, "CustomPool")

            runBlocking(dispatcher) {
                repeat(2) {
                    launch {
                        isDaemonFlags.add(Thread.currentThread().isDaemon)
                    }
                }
            }

            isDaemonFlags.all { it } shouldBe true
        }

        "자식 코루틴은 자신의 코루틴 디스패처가 설정되지 않으면 부모 코루틴의 코루틴 디스패처를 사용한다" {
            val dispatcher = newFixedThreadPoolContext(2, "CustomPool")
            val parentDispatcherThread: String
            val childDispatcherThread: String

            runBlocking(dispatcher) {
                parentDispatcherThread = Thread.currentThread().name

                childDispatcherThread =
                    launch {
                        delay(10)
                        Thread.currentThread().name
                    }.let {
                        it.join()
                        Thread.currentThread().name
                    }
            }

            childDispatcherThread.contains("CustomPool")
            parentDispatcherThread.contains("CustomPool")
        }

        "미리 정의된 디스패처를 사용하는 것이 권장된다" - {
            "Dispatchers.IO 는 I/O bound 작업 시 사용하며 스레드는 데몬 스레드이다" {
                var isDaemon: Boolean
                runBlocking(Dispatchers.IO) {
                    isDaemon = Thread.currentThread().isDaemon
                }
                isDaemon shouldBe true
            }

            "Dispatchers.Default 는 CPU bound 작업 시 사용하며 스레드는 데몬 스레드이다" {
                var isDaemon: Boolean
                runBlocking(Dispatchers.Default) {
                    isDaemon = Thread.currentThread().isDaemon
                }
                isDaemon shouldBe true
            }

            "Dispatchers.Default.limitedParallelism 을 사용하면 병렬로 사용할 스레드 수를 제한할 수 있다" {
                var isDaemon: Boolean
                val limitedDispatcher = Dispatchers.Default.limitedParallelism(2)
                runBlocking(limitedDispatcher) {
                    isDaemon = Thread.currentThread().isDaemon
                }
                isDaemon shouldBe true
            }

            "Dispatchers.Default 와 Dispatchers.IO 는 같은 공유 스레드풀을 사용한다" {
                val defaultThreadNames = mutableSetOf<String>()
                val ioThreadNames = mutableSetOf<String>()
                val limitedDefaultThreadNames = mutableSetOf<String>()
                val limitedIOThreadNames = mutableSetOf<String>()
                val customPoolThreadNames = mutableSetOf<String>()

                runBlocking {
                    List(4) {
                        launch(Dispatchers.Default) {
                            delay(10)
                            synchronized(defaultThreadNames) {
                                defaultThreadNames.add(Thread.currentThread().name)
                            }
                        }
                    }

                    List(4) {
                        launch(Dispatchers.Default.limitedParallelism(2)) {
                            delay(10)
                            synchronized(defaultThreadNames) {
                                defaultThreadNames.add(Thread.currentThread().name)
                            }
                        }
                    }

                    List(4) {
                        launch(Dispatchers.IO) {
                            delay(10)
                            synchronized(ioThreadNames) {
                                ioThreadNames.add(Thread.currentThread().name)
                            }
                        }
                    }

                    List(4) {
                        launch(Dispatchers.IO.limitedParallelism(4)) {
                            delay(10)
                            synchronized(ioThreadNames) {
                                ioThreadNames.add(Thread.currentThread().name)
                            }
                        }
                    }

                    List(2) {
                        launch(newFixedThreadPoolContext(2, "CustomPool")) {
                            delay(10)
                            synchronized(ioThreadNames) {
                                customPoolThreadNames.add(Thread.currentThread().name)
                            }
                        }
                    }
                }

                defaultThreadNames.all { it.startsWith("DefaultDispatcher") } shouldBe true
                ioThreadNames.all { it.startsWith("DefaultDispatcher") } shouldBe true
                limitedDefaultThreadNames.all { it.startsWith("DefaultDispatcher") } shouldBe true
                limitedIOThreadNames.all { it.startsWith("DefaultDispatcher") } shouldBe true
                customPoolThreadNames.all { it.startsWith("CustomPool") } shouldBe true
            }
        }
    }
})
