package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class CoroutineSuspendTest : FreeSpec({

    "일시 중단 함수는 코루틴이 이니다." {
        suspend fun delayAndPrintHelloWorld() {
            delay(1000L)
            println("Hello World")
        }

        val rootJob =
            launch {
                val startTime = System.currentTimeMillis()
                delayAndPrintHelloWorld()
                delayAndPrintHelloWorld()
                elapsedTimeMilli(startTime) shouldBeGreaterThanOrEqualTo 2000L
                // 만약 일시 중단 함수가 코루틴이라면 delayAndPrintHelloWorld() 메서드가 동시에 실행되어야 한다.
            }
    }

    "suspend 함수 내에서 coroutineScope 메서드를 사용하여 코루틴의 구조화를 깨지 않는 CoroutineScope 객체를 생성할 수 있다" {
        val rootJob =
            launch {
                val startTime = System.currentTimeMillis()
                searched("Keyword")
                elapsedTimeMilli(startTime) shouldBeLessThan 500L
            }
        rootJob.join()
    }

    "suspend 함수 내에서 supervisorScope 메서드를 사용하여 코루틴의 구조화를 깨지 않으면서 예외를 전파하지 않는 CoroutineScope 객체를 생성할 수 있다" {
        val rootJob =
            launch {
                val searched = supervisorSearched("Keyword")
                searched shouldBe arrayOf("[Server]Keyword1", "[Server]Keyword2")
            }.join()
    }
})

//
suspend fun searched(keyword: String): Array<String> =
    coroutineScope {
        val dbResults =
            async {
                searchedFromDB(keyword)
            }
        val serverResults =
            async {
                searchedFromServer(keyword)
            }
        return@coroutineScope arrayOf(*dbResults.await(), *serverResults.await())
    }

suspend fun searchedFromDB(keyword: String): Array<String> {
    delay(100L)
    return arrayOf("[DB]${keyword}1, [DB]${keyword}2")
}

suspend fun searchedFromServer(keyword: String): Array<String> {
    delay(100L)
    return arrayOf("[Server]${keyword}1", "[Server]${keyword}2")
}

suspend fun supervisorSearched(keyword: String): Array<String> =
    supervisorScope {
        val dbResultsDeferred =
            async {
                throw Exception("DB Error")
                @Suppress("UNREACHABLE_CODE")
                searchedFromDB(keyword)
            }

        val serverResultsDeferred =
            async {
                searchedFromServer(keyword)
            }

        val dbRestults =
            try {
                dbResultsDeferred.await()
            } catch (e: Exception) {
                arrayOf()
            }

        val serverResults =
            try {
                serverResultsDeferred.await()
            } catch (e: Exception) {
                arrayOf()
            }
        return@supervisorScope arrayOf(*dbRestults, *serverResults)
    }
