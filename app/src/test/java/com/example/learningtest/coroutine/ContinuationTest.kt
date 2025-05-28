package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread

/*
* Continuation 객체는 코루틴의 실행에 매우 핵심적인 역할을 한다.
* 코루틴 라이브러리에서 제공하는 고수준 코루틴 API 는 모두 Continuation 객체를 캡슐화해 사용자에게 노출하지 않는다.
* 하지만 내부적으로는 코루틴의 일시 중단과 재개가 Continuation 객체를 통해 이루어 진다.
* */
@OptIn(ExperimentalCoroutinesApi::class)
class ContinuationTest : FreeSpec({

    "suspendCancellableCoroutine은 continuation 객체를 CancellableContinuation 타입으로 제공한다" {
        launch(CoroutineName("root")) {
            suspendCancellableCoroutine { continuation: CancellableContinuation<Unit> ->
                continuation.context[CoroutineName]?.name shouldBe "root"
                continuation.resume(Unit) {} // 코루틴 재개를 호출하지 않으면 프로세스가 종료되지 않음.
            }
        }
    }

    "코루틴 재개 시 다른 작업으로부터 결과를 수신받아야 하는 경우에는 " +
        "suspendCancellableCoroutine 함수의 타입 인자에 결과로 반환받는 타입을 입력하면 된다" {
            val rootJob =
                launch(CoroutineName("root")) {
                    val result =
                        suspendCancellableCoroutine<String> { continuation: CancellableContinuation<String> ->
                            thread {
                                Thread.sleep(100L)
                                continuation.resume("result") {}
                            }
                        }
                    result shouldBe "result"
                }
            rootJob.join()
        }
})
