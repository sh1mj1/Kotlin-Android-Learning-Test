package com.example.learningtest.context

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import com.example.learningtest.R
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmName

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApplication::class)
class ContextBasicTest {
    private val activity1Controller = Robolectric.buildActivity(StubActivity1::class.java)
    private val activity2Controller = Robolectric.buildActivity(StubActivity2::class.java)
    private val activity1 = activity1Controller.get()
    private val activity2 = activity2Controller.get()

    private val service1Controller = Robolectric.buildService(StubService1::class.java)
    private val service2Controller = Robolectric.buildService(StubService2::class.java)
    private val service1 = service1Controller.get()
    private val service2 = service2Controller.get()

    private val application = ApplicationProvider.getApplicationContext<StubApplication>()

    @Test
    fun `activity extends ContextWrapper`() {
        activity1.shouldBeInstanceOf<ContextWrapper>()
    }

    @Test
    fun `service extends ContextWrapper`() {
        service1.shouldBeInstanceOf<ContextWrapper>()
    }

    @Test
    fun `application extends ContextWrapper`() {
        application.shouldBeInstanceOf<ContextWrapper>()
    }

    @Test
    fun `ContextWrapper extends Context`() {
        (activity1 as ContextWrapper).shouldBeInstanceOf<Context>()
        (service1 as ContextWrapper).shouldBeInstanceOf<Context>()
        (application as ContextWrapper).shouldBeInstanceOf<Context>()
    }

    @Test
    fun `can get android resources such as string, drawable through context`() {
        activity1.getString(R.string.app_name) shouldBe "LearningTest"
        activity1.getDrawable(R.drawable.ic_launcher_foreground) shouldNotBe null

        service1.getString(R.string.app_name) shouldBe "LearningTest"
        service1.getDrawable(R.drawable.ic_launcher_foreground) shouldNotBe null

        application.getString(R.string.app_name) shouldBe "LearningTest"
        application.getDrawable(R.drawable.ic_launcher_foreground) shouldNotBe null
    }

    @Test
    fun `the application context is singleton`() {
        val applicationContexts =
            listOf(
                activity1.applicationContext,
                activity2.applicationContext,
                service1.applicationContext,
                service2.applicationContext,
            ).distinct()
        applicationContexts.size shouldBe 1
        applicationContexts.first() shouldBe application.applicationContext
    }

    @Test
    fun `ContextWrapper has ContextImpl as baseContext`() {
        val baseContextField =
            ContextWrapper::class.memberProperties.find { it.name == "mBase" }
                ?: error("mBase field not found")
        baseContextField.isAccessible = true

        val baseContext = baseContextField.get(activity1) ?: error("baseContext is null")
        val kClassName = baseContext::class.jvmName

        kClassName shouldBe "android.app.ContextImpl"
    }

    @Test
    fun `baseContext of activity, service, application is ContextImpl instance`() {
        activity1.baseContext::class.jvmName shouldBe "android.app.ContextImpl"
        service1.baseContext::class.jvmName shouldBe "android.app.ContextImpl"
        application.baseContext::class.jvmName shouldBe "android.app.ContextImpl"
    }
}

class StubApplication : Application()

private class StubActivity1 : ComponentActivity()

private class StubActivity2 : ComponentActivity()

private class StubService1 : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}

private class StubService2 : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
