package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CoroutineBuilderAsyncTest : FreeSpec({
    "#1 async 가 순차적으로 실행되는 코드 구조" {
        val startTime = System.currentTimeMillis()
        val deferred1: Deferred<String> =
            async {
                delay(1000L)
                return@async "result1"
            }

        deferred1.await()

        val deferred2: Deferred<String> =
            async {
                delay(1000L)
                return@async "result2"
            }
        deferred2.await()

        elapsedTimeMilli(startTime) shouldBeGreaterThanOrEqual 2000
    }

    "#2 async 가 동시에 실행되는 코드 구조 2" {
        val startTime = System.currentTimeMillis()
        val deferred1: Deferred<String> =
            async {
                delay(1000L)
                return@async "result1"
            }
        val deferred2: Deferred<String> =
            async {
                delay(1000L)
                return@async "result2"
            }

        deferred1.await()
        deferred2.await()

        elapsedTimeMilli(startTime) shouldBeLessThan 2000
    }

    "awaitAll 을 통해 여러 async 의 결과를 한 번에 기다릴 수 있다" {
        val startTime = System.currentTimeMillis()
        val deferred1: Deferred<String> =
            async {
                delay(500L)
                return@async "result1"
            }
        val deferred2: Deferred<String> =
            async {
                delay(100L)
                return@async "result2"
            }

        val results = awaitAll(deferred1, deferred2)

        elapsedTimeMilli(startTime) shouldBeLessThan 2000

        results.size shouldBe 2
        results[0] shouldBe "result1"
        results[1] shouldBe "result2"
    }
    "awaitAll 은 컬렉션의 확장함수로도 사용할 수 있다" {
        val deferred1: Deferred<String> =
            async {
                delay(100L)
                return@async "result1"
            }
        val deferred2: Deferred<String> =
            async {
                delay(100L)
                return@async "result2"
            }

        val results: List<String> = listOf(deferred1, deferred2).awaitAll()

        results.size shouldBe 2
        results[0] shouldBe "result1"
        results[1] shouldBe "result2"
    }

    "async-await 쌍으로 네트워크 통신" {
        val networkDeferred: Deferred<String> =
            async(Dispatchers.IO) {
                delay(1000L)
                return@async "Dummy Response"
            }
        val result = networkDeferred.await()
        println(result)
    }

    "withContext 로 async-await 를 대체할 수 있다 withContext 는 코루틴 컨텍스트만 변경하고 새 코루틴을 만들지는 않는다" {
        // pool-1-thread-1 @coroutine#8 외부 블록
        val networkDeferred: Deferred<String> =
            async(Dispatchers.IO) {
                networkSimulation()
                // DefaultDispatcher-worker-1 @coroutine#9 async block
                return@async "Dummy Response"
            }

        networkDeferred.await() shouldBe "Dummy Response"

        val result: String =
            withContext(Dispatchers.IO) {
                networkSimulation()
                // DefaultDispatcher-worker-1 @coroutine#8 withContext block
                return@withContext "Dummy Response"
            }

        result shouldBe "Dummy Response"
    }
})

suspend fun networkSimulation() {
    delay(100L)
}
