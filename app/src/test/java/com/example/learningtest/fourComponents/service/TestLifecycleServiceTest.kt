package com.example.learningtest.fourComponents.service

import android.content.Intent
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TestLifecycleServiceTest {
    @Before
    fun setUp() {
        TestLifecycleService.log.clear()
    }

    @Test
    fun `startService 호출 시 onCreate 와 onStartCommand 가 호출된다`() {
        val context = RuntimeEnvironment.getApplication()
        val intent = Intent(context, TestLifecycleService::class.java)

        Robolectric.buildService(TestLifecycleService::class.java, intent)
            .create()
            .startCommand(0, 0)

        TestLifecycleService.log shouldBe listOf("onCreate", "onStartCommand")
    }

    @Test
    fun `서비스는 stopSelf 호출 시 onDestroy 가 호출된다`() {
        val context = RuntimeEnvironment.getApplication()
        val intent = Intent(context, TestLifecycleService::class.java)

        val controller =
            Robolectric.buildService(TestLifecycleService::class.java, intent)
                .create().startCommand(0, 0)
        val service = controller.get()

        service.stopSelf()
        controller.destroy()

        TestLifecycleService.log shouldBe listOf("onCreate", "onStartCommand", "onDestroy")
    }
}
