package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineScopeTest : FreeSpec({
    "코루틴 스코프를 사용해 코루틴 빌더 함수를 호출하면 코루틴 스코프 객체로부터 실행 환경을 제공받는 새로운 코루틴을 실행한다 " {
        val coroutineScope = CustomCoroutineScope()

        coroutineScope.launch {
            delay(100L)
            coroutineContext[CoroutineName]?.name shouldBe "myCoroutine"
        }
    }

    "가짜 생성자 함수 CoroutineScope 를 사용하여 코루틴 스코프 객체를 만들 수 있다" {
        val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineName("myCoroutine"))

        coroutineScope.launch {
            delay(100L)
            println("${Thread.currentThread().name} 코루틴 실행 완료")
            coroutineContext[CoroutineName]?.name shouldBe "myCoroutine"
        }
    }

    "코루틴 스코프 내에서 launch 함수를 호출하면, 새 코루틴의 Job은 해당 스코프의 Job을 부모로 가진다" {
        val newScope = CoroutineScope(CoroutineName("MyCoroutine") + Dispatchers.IO)
        newScope.coroutineContext[CoroutineName]?.name shouldBe "MyCoroutine"

        newScope.launch(CoroutineName("launch1")) {
            this.coroutineContext[CoroutineName]?.name shouldBe "launch1"

            val launchJob = this.coroutineContext[Job]
            val newScopeJob = newScope.coroutineContext[Job]

            newScopeJob shouldNotBe launchJob
            newScopeJob?.parent shouldBe launchJob
        }
        // 이는 부모 코루틴이 자식 코루틴으로 실행 환경을 상속하는 방식과 완전히 동일하다.
        // 실제로 코루틴이 부모 코루틴의 CoroutineContext 객체를 가진 CoroutineScope 객체로부터 실행 환경을 상속받기 때문이다.
    }

    "코루틴 빌더 함수의 body 내에서 새로운 코루틴 스코프를 직접 만들어 부모의 코루틴 스코프에서 벗어날 수 있다" {
        launch {
            val parentJob = coroutineContext[Job]
            launch {
                coroutineContext[Job]?.parent?.job shouldBe parentJob
            }
            CoroutineScope(Dispatchers.IO).launch {
                coroutineContext[Job]?.parent?.job shouldNotBe parentJob
                // 코루틴의 구조화를 깨는 것은 비동기 작업을 안전하지 않게 만든다.
                // 특수한 목적이 있는 게 아니라면, 최대한 지양하자.
            }
        }
    }

    "코루틴 스코프의 cancel 함수는 코루틴 스코프 객체의 범위에 속한 모든 코루틴을 취소한다" {
        var coroutineIsCompletedWell1 = false
        var coroutineIsCompletedWell2 = false
        var coroutineIsCompletedWell3 = false
        launch {
            launch {
                delay(100L)
                coroutineIsCompletedWell1 = true
            }
            launch {
                delay(100L)
                coroutineIsCompletedWell2 = false
            }
            CoroutineScope(Dispatchers.IO).launch {
                delay(100L)
                coroutineIsCompletedWell3 = true
            }
            this.cancel()
        }
        delay(500L)
        coroutineIsCompletedWell1 shouldBe false
        coroutineIsCompletedWell2 shouldBe false
        coroutineIsCompletedWell3 shouldBe true
    }
})

class CustomCoroutineScope : CoroutineScope {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    override val coroutineContext: CoroutineContext =
        Job() + newSingleThreadContext("CustomScopeThread") + CoroutineName("myCoroutine")
}
