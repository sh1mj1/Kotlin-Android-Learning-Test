package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.sequences.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

/*
* 구조화된 동시성: 코루틴을 부모-자식 관계로 구조화함으로써 코루틴이 보다 안전하게 관리되고 제어될 수 있도록 한다.
* 부모의 실행환경은 자식에게 상속된다
* 부모가 취소하면 자식도 취소된다
* 부모는 자식이 완료될 때까지 기다린다
* CoroutineScope 를 사용해서 코루틴이 실행되는 범위를 제한할 수 있다.
* */
@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
class StructuredConcurrencyTest : FreeSpec({

    "부모 코루틴의 실행 환경은 자식에게 상속된다" {
        val myContext = newSingleThreadContext("1") + CoroutineName("1")

        launch(myContext) {
            this.coroutineContext[CoroutineName] shouldBe myContext[CoroutineName]
            this.coroutineContext[CoroutineDispatcher] shouldBe myContext[CoroutineDispatcher]
            launch {
                this.coroutineContext[CoroutineName] shouldBe myContext[CoroutineName]
                this.coroutineContext[CoroutineDispatcher] shouldBe myContext[CoroutineDispatcher]
            }
        }
    }
    "자식 코루틴에 새로운 실행환경이 전달되면 부모의 실행환경은 덮어씌워진다" {
        val myContext = newSingleThreadContext("1") + CoroutineName("1")

        launch(myContext) {
            this.coroutineContext[CoroutineName] shouldBe myContext[CoroutineName]
            this.coroutineContext[CoroutineDispatcher] shouldBe myContext[CoroutineDispatcher]
            launch(CoroutineName("2")) {
                this.coroutineContext[CoroutineName] shouldNotBe myContext[CoroutineName]
                this.coroutineContext[CoroutineName]?.name shouldBe "2"
                this.coroutineContext[CoroutineDispatcher] shouldBe myContext[CoroutineDispatcher]
            }
        }
    }

    "모든 코루틴 빌더 함수는 호출 때마다 Job 객체를 새롭게 생성한다" {
        val myContext = newSingleThreadContext("1") + CoroutineName("1")
        var outerJob: Job? = null
        launch(myContext) {
            outerJob = this.coroutineContext[Job]!!
            launch {
                val innerJob = this.coroutineContext[Job]!!

                outerJob shouldNotBe innerJob
            }
        }
    }

    "자식 코루틴 Job 객체는 parent 프로퍼티를 통해, 부모 코루틴 Job 객체는 children 프로퍼티를 통해 양방향 참조를 가진다" {
        val myContext = newSingleThreadContext("1") + CoroutineName("1")
        launch(myContext) {
            val parentJob = this.coroutineContext[Job] ?: error("Job이 존재하지 않습니다.")
            launch {
                val childJobInside = this.coroutineContext[Job] ?: error("Job이 존재하지 않습니다.")

                val children: Sequence<Job> = parentJob.children
                val parentInside: Job? = childJobInside.parent

                parentInside shouldBe parentJob
                children shouldContain childJobInside
            }
        }
    }
})
