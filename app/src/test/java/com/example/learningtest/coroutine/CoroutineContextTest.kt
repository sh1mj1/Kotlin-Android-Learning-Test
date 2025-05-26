package com.example.learningtest.coroutine

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.full.allSuperclasses

/*
* CoroutineName 객체와 CoroutineDispatcher 는 CoroutineContext 객체의 구성요소이다.
* 대표적으로 CoroutineName, CoroutineDispatcher, Job, CoroutineExceptionHanlder 가 있다.
* 각 객체는 하나씩 만 가질 수 있고 key-value 쌍이다.
*  */
@OptIn(ExperimentalStdlibApi::class, DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
class CoroutineContextTest : FreeSpec({

    "CoroutineContext 는 CoroutineName, CoroutineDispatcher, Job, CoroutineExceptionHandler 를 + 연산으로 설정할 수 있다" {
        val coroutineContext: CoroutineContext =
            newSingleThreadContext("MyThread") + CoroutineName("MyCoroutine")
        coroutineContext[CoroutineName]?.name shouldBe "MyCoroutine"
        coroutineContext[CoroutineDispatcher].shouldBeInstanceOf<CoroutineDispatcher>()
        coroutineContext[Job] shouldBe null
        coroutineContext[CoroutineExceptionHandler] shouldBe null
    }

    "CoroutineContext 의 요소들을 꺼내올 때는 키를 통해 접근한다" {
        val coroutineContext: CoroutineContext =
            newSingleThreadContext("MyThread") + CoroutineName("MyCoroutine")

        coroutineContext[CoroutineName]?.name shouldBe "MyCoroutine"
        coroutineContext[CoroutineDispatcher.Key] shouldBe coroutineContext[CoroutineDispatcher]
    }

    "CoroutineName, CoroutineDiaptcher, Job, CoroutineExceptionHandler 는 모두 CoroutineContext 를 상속한다" {
        CoroutineName::class.allSuperclasses shouldContain CoroutineContext::class
        CoroutineDispatcher::class.allSuperclasses shouldContain CoroutineContext::class
        Job::class.allSuperclasses shouldContain CoroutineContext::class
        CoroutineExceptionHandler::class.allSuperclasses shouldContain CoroutineContext::class
    }

    "구성요소가 없는 CoroutineContext" {
        val emptyCoroutineContext: CoroutineContext = EmptyCoroutineContext
        emptyCoroutineContext[CoroutineName] shouldBe null
        emptyCoroutineContext[CoroutineDispatcher] shouldBe null
        emptyCoroutineContext[Job] shouldBe null
        emptyCoroutineContext[CoroutineExceptionHandler] shouldBe null
    }

    "CoroutineContext 객체에 같은 구성 요소가 둘 이상 더해진다면 나중에 추가된 CoroutineContext 구성 요소가 값을 덮어쓴다" {
        val coroutineContext: CoroutineContext =
            newSingleThreadContext("MyThread") + CoroutineName("OldName")
        val newCoroutineContext: CoroutineContext = coroutineContext + CoroutineName("NewName")

        newCoroutineContext[CoroutineName]?.name shouldBe "NewName"
    }

    "CoroutineContext 객체를 합치면 겹치는 속성은 모두 덮어씌워진다." {
        val job: Job =
            launch {
                delay(100L)
            }
        val coroutineContext1 =
            CoroutineName("MyCoroutine1") + newSingleThreadContext("MyThread1") + job
        val coroutineContext2 = CoroutineName("MyCoroutine2") + newSingleThreadContext("MyThread2")

        val newCoroutineContext = coroutineContext1 + coroutineContext2

        newCoroutineContext[CoroutineName]?.name shouldBe "MyCoroutine2"
        newCoroutineContext[CoroutineDispatcher] shouldBe coroutineContext2[CoroutineDispatcher]
        newCoroutineContext[Job] shouldBe job
    }

    "CoroutineContext 의 구성 요소들은 key 프로퍼티를 가진다 이 key 들은 각 종류에 따라 하나의 고유한 Key 를 가진다" {
        val coroutineName1 = CoroutineName("1")
        val coroutineName2 = CoroutineName("2")

        val coroutineDispatcher1 = newSingleThreadContext("1")
        val coroutineDispatcher2 = newSingleThreadContext("2")

        coroutineName1.key shouldBe coroutineName2.key
        coroutineDispatcher1.key shouldBe coroutineDispatcher2.key

        coroutineName1.key shouldNotBe coroutineDispatcher2.key
    }

    "CoroutineContext 의 구성요소는 minusKey 를 사용해 제거할 수 있다" {
        val job = Job()
        val coroutineContext = CoroutineName("1") + Dispatchers.IO + job

        val deleted = coroutineContext.minusKey(job.key)

        coroutineContext[CoroutineName]?.name shouldBe "1"
        coroutineContext[CoroutineDispatcher] shouldBe Dispatchers.IO
        coroutineContext[Job] shouldBe job

        deleted[CoroutineName]?.name shouldBe "1"
        deleted[CoroutineDispatcher] shouldBe Dispatchers.IO
        deleted[Job] shouldBe null
    }
})
