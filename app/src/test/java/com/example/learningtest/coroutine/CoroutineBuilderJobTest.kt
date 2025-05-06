package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

        (logs == listOf("이미지1 변환 완료", "이미지2 변환 완료", "이미지1,2 업로드") || logs == listOf(
            "이미지2 변환 완료",
            "이미지1 변환 완료",
            "이미지1,2 업로드"
        )) shouldBe true
    }
})
