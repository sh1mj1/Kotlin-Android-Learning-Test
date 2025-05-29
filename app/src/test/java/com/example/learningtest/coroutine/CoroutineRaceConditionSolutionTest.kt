package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.Volatile

class CoroutineRaceConditionSolutionTest : FreeSpec({

    "Volatile 로 메모리 가시성 문제 해결, Mutex 로 경쟁 상태 해결" - {
        "lock 과 unlock 메서드 사용" {
            val rootJob =
                launch {
                    withContext(Dispatchers.Default) {
                        repeat(10_000) {
                            launch {
                                mutex.lock()
                                count1 += 1
                                mutex.unlock()
                            }
                        }
                    }
                }
            rootJob.join()
            count1 shouldBe 10_000
        }
        "withLock 메서드 사용" {
            count1 = 0
            val rootJob =
                launch {
                    withContext(Dispatchers.Default) {
                        repeat(10_000) {
                            launch {
                                mutex.withLock {
                                    count1 += 1
                                }
                            }
                        }
                    }
                }
            rootJob.join()
            count1 shouldBe 10_000
        }
    }

    "공유 상태 변경을 위해 전용 스레드 사용하기" {
        val rootJob =
            launch {
                withContext(Dispatchers.Default) {
                    repeat(10_000) {
                        launch {
                            incraseCount()
                        }
                    }
                }
            }
        rootJob.join()

        count2 shouldBe 10_000
    }

    /*
     * 원자성 있는 객체를 코루틴에 사용할 때는 원자성 있는 객체가 스레드를 블로킹시킬 수 있다.
     * 원자성 있는 객체를 사용할 때 객체의 읽기와 쓰기를 따로 실행하는 실수를 하면 안된다.
     * getAndUpdate 와 같은 읽기와 쓰기를 함께 사용하는 함수를 사용해야 한다.
     * 즉, get 과 set 을 따로 사용하면 안 된다.
     * */
    "원자성 있는 데이터 구조를 사용한 경쟁 상태 문제 해결" - {
        "간단한 타입(기본 타입)에 원자성 있는 데이터 구조를 사용" {
            val rootJob =
                launch {
                    withContext(Dispatchers.Default) {
                        repeat(10_000) {
                            launch {
                                count3.getAndUpdate {
                                    it + 1
                                }
                            }
                        }
                    }
                }
            rootJob.join()
            count3.get() shouldBe 10_000
        }

        "객체 참조 타입에 원자성 있는 데이터 구조를 사용" {
            val rootJob =
                launch {
                    withContext(Dispatchers.Default) {
                        repeat(10_000) {
                            launch {
                                atomicCounter.getAndUpdate {
                                    it.copy(count = it.count + 1)
                                }
                            }
                        }
                    }
                }
            rootJob.join()
            atomicCounter.get() shouldBe Counter("Mine", 10_000)
        }
    }
})

data class Counter(
    val name: String,
    val count: Int,
)

val atomicCounter =
    AtomicReference(
        Counter("Mine", 0),
    )

private suspend fun incraseCount() =
    coroutineScope {
        withContext(countChangeDispatcher) {
            count2 += 1
        }
    }

@Volatile
private var count1 = 0
private val mutex = Mutex()

var count2 = 0

@OptIn(ExperimentalCoroutinesApi::class)
val countChangeDispatcher = Dispatchers.IO.limitedParallelism(1)
// 혹은 newSingleThreadContext("CountChangeThread") 사용 가능.

var count3 = AtomicInteger(0)
