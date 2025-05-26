package com.example.learningtest.coroutine

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
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

    "CoroutineExceptionHandler 를 사용한 예외 처리" - {

        /*
         * 마지막으로 예외를 전파받는 위치(예외가 처리되는 위치)에 설정된 CoroutineExceptionhandler 객체만 예외를 처리한다.
         * */
        "에외를 부모 코루틴으로 전파하고 전파받은 코루틴의 exceptionHandler 가 처리한다".config(enabled = false) {
            val rootJob =
                launch {
                    launch(CoroutineName("1") + myExceptionhandler) {
                        throw Exception("1 에 예외가 발생했습니다.")
                    }
                    delay(100L)
                }
            rootJob.join()
        }

        "자식 코루틴에서 발생한 예외는 부모 CoroutineScope의 CoroutineExceptionHandler가 처리한다" {
            var exceptionIsCaught = false
            val myExceptionhandler =
                CoroutineExceptionHandler { couroutineContext, throwable ->
                    println("[예외 발생] $throwable")
                    exceptionIsCaught = true
                }

            val rootJob =
                launch {
                    CoroutineScope(myExceptionhandler).launch(CoroutineName("1")) {
                        throw Exception("1 에서 예외 발생")
                    }
                    delay(100L)
                }
            rootJob.join()
            exceptionIsCaught shouldBe true
        }

        "CoroutineExceptionHandler 객체를 Job 과 함께 설정하여 간단히 예외를 처리할 수 있다." {
            var exceptionIsCaught = false
            val job =
                launch {
                    val coroutineContext =
                        Job() +
                            CoroutineExceptionHandler { coroutineContext, throwable ->
                                println("[예외 발생] $throwable")
                                exceptionIsCaught = true
                            }
                    launch(CoroutineName("1") + coroutineContext) {
                        throw Exception("1 에서 예외 발생")
                    }
                    delay(100L)
                }
            job.join()
            exceptionIsCaught shouldBe true
        }

        /*
         * SupervisorJob 은 예외를 전파받지 않아도 예외에 대한 정보는 전달받는다.
         * SupervisorJob 의 자식 코루틴에서 예외가 발생하면 부모에게 예외를 전파하지 않더라도 예외 정보를 전달받는다.
         * 이 때 자식 코루틴이 예외 정보 전달만 해도 자식 코루틴은 예외를 처리된 것으로 본다.
         * */
        "SupervisorJob과 CoroutineExceptionHandler를 함께 사용하여 예외 처리 및 다른 코루틴의 정상 작동 보장" {
            var coroutine2CompletedWell = false

            var exceptionIsCaught = false
            val myExceptionhandler =
                CoroutineExceptionHandler { couroutineContext, throwable ->
                    println("[예외 발생] $throwable")
                    exceptionIsCaught = true
                }

            val job =
                launch {
                    CoroutineScope(SupervisorJob() + myExceptionhandler).apply {
                        launch(CoroutineName("1")) {
                            throw Exception("1 에서 예외 발생")
                        }
                        launch(CoroutineName("2")) {
                            delay(100L)
                            coroutine2CompletedWell = true
                        }
                    }
                    delay(100L)
                }

            job.join()
            exceptionIsCaught shouldBe true
            coroutine2CompletedWell shouldBe true
        }

        "CoroutineExceptionhandler 는 예외 전파를 제한하지 않는다".config(enabled = false) {
            val myExceptionHandler =
                CoroutineExceptionHandler { context, throwable ->
                    println("[예외 발생] $throwable")
                }

            val job =
                launch {
                    launch(CoroutineName("1") + myExceptionHandler) {
                        throw Exception("1 에 예외가 발생했습니다.")
                    }
                }
        }
    }
    "try-catch 문을 사용한 예외 처리" - {
        "코루틴 내부의 try-catch 블록은 예외를 처리하고 다른 코루틴의 실행에 영향을 주지 않는다" {
            var coroutine2CompletedWell = false
            val rootJob =
                launch {
                    launch(CoroutineName("1")) {
                        try {
                            throw Exception("1에서 예외가 발생했습니다")
                        } catch (e: Exception) {
                            println(e.message)
                        }
                    }
                    launch(CoroutineName("2")) {
                        delay(100L)
                        coroutine2CompletedWell = true
                    }
                }
            rootJob.join()

            coroutine2CompletedWell shouldBe true
        }

        /*
         * try catch 문을 코루틴 빌더 함수에 사용하면 코루틴에서 발생한 예외가 잡히지 않는다.
         * launch 는 코루틴을 생성하는데 사용되는 함수일 뿐으로
         * 람다식의 실행은 생성된 코루틴이 CoroutineDispatcher 에 의해 스레드로 분배되는 시점에 일어나기 때문이다.
         * 아래 try catch 문은 launch 코루틴 빌더 함수 자체의 실행만 체크하며 람다식은 예외 처리 대상이 아니다.
         * 즉, 코루틴에 대한 예외 처리를 위해서는 코루틴 빌더 함수의 람다식 내부에서 try catch 문을 사용해야 한다.
         * */
        "코루틴 빌더 함수에 대한 try catch 문은 코루틴의 예외를 잡지 못한다".config(enabled = false) {
            val rootJob =
                launch {
                    try {
                        launch(CoroutineName("1")) {
                            throw Exception("1에서 예외가 발생했습니다")
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                    launch(CoroutineName("2")) {
                        delay(100L)
                    }
                }
            rootJob.join()
        }
    }

    "async 의 예외 처리" - {
        "async 코루틴 빌더로 만들어진 코루틴에서 예외 발생 시 await 호출부에서 예외 처리가 될 수 있게 해야 한다" {
            var exceptionIsCaught = false
            val rootJob =
                launch {
                    supervisorScope {
                        val deferred =
                            async(CoroutineName("1")) {
                                throw Exception("1에서 예외가 발생했습니다")
                            }
                        try {
                            deferred.await()
                        } catch (e: Exception) {
                            exceptionIsCaught = true
                        }
                    }
                }
            rootJob.join()

            exceptionIsCaught shouldBe true
        }

        "async 도 예외 전파 처리를 하지 않으면 부모 코루틴으로 예외가 전파되고, 취소가 자식 코루틴으로 전파된다" {
            val rootJob =
                launch {
                    async(CoroutineName("1")) {
                        throw Exception("1 에서 예외 발생")
                    }
                    launch(CoroutineName("2")) {
                        delay(100L)
                        println("[${Thread.currentThread().name}] 코루틴 실행")
                    }
                }
        }

        "async 코루틴 빌더를 사용할 때는 전파되는 예외와 await 호출 시 노출되는 예외를 모두 처리해 주어야 한다" {
            val rootJob =
                launch {
                    supervisorScope {
                        async(CoroutineName("1")) {
                            throw Exception("1 에서 예외 발생")
                        }
                        launch(CoroutineName("2")) {
                            delay(100L)
                            println("[${Thread.currentThread().name}] 코루틴 실행")
                        }
                    }
                }
        }
    }
})

val myExceptionhandler =
    CoroutineExceptionHandler { couroutineContext, throwable ->
        println("[예외 발생] $throwable")
    }
