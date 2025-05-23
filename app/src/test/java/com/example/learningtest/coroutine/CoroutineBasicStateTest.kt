package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.full.memberProperties

/*
* 코루틴의 상태 중 아래 5가지를 다룬다
* 생성(New), 실행 중(Active), 실행 완료(Completed), 취소 중(Cancelling), 취소 완료(Cancelled)*/
class CoroutineBasicStateTest : FreeSpec({
    "Job 객체에서 외부로 공개하는 코루틴 상태 변수는 isActive, isCancelled, isCompleted 로 접근할 수 있다." {
        val jobProperties = Job::class.memberProperties.map { it.name }

        jobProperties shouldContainAll listOf("isActive", "isCancelled", "isCompleted")
    }

    "생성 상태 코루틴은 isActive, isCancelled, isCompleted 가 모두 거짓이다" {
        val job: Job =
            launch(start = CoroutineStart.LAZY) {
                delay(100L)
            }

        with(job) {
            isActive shouldBe false
            isCancelled shouldBe false
            isCompleted shouldBe false
        }

        job.cancel()
    }

    "코루틴이 실행된 후 취소되거나 완료되지 않은 경우 isActive 만 참이다" {
        val job: Job =
            launch {
                delay(100L)
            }

        with(job) {
            isActive shouldBe true
            isCancelled shouldBe false
            isCompleted shouldBe false
        }
    }

    "코루틴이 실행 완료된 상태이면 isCompleted 가 참이다" {
        val job: Job =
            launch {
                delay(100L)
            }
        delay(400L)

        with(job) {
            isActive shouldBe false
            isCancelled shouldBe false
            isCompleted shouldBe true
        }
    }

    "코루틴에 취소가 요청되었으나 취소되지 않은 상태" {
        val whileJob: Job =
            launch {
                while (true) {
                    // 여기서 코루틴이 양보할 지점을 만들어주지 않음으로써 취소 요청이 되어도 취소되지 않도록 함.
                }
            }
        whileJob.cancel()
        with(whileJob) {
            isActive shouldBe false
            isCancelled shouldBe true
            isCompleted shouldBe false
        }
    }

    "코루틴에 요청된 취소를 확인한 후 취소가 완료된 상태" {
        val job: Job =
            launch {
                delay(5000L)
            }
        job.cancelAndJoin()
        with(job) {
            isActive shouldBe false
            isCancelled shouldBe true
            isCompleted shouldBe true
        }
    }
})
