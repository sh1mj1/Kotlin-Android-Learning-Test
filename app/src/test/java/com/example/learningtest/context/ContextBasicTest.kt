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

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApplication::class)
class ContextBasicTest {
    private val activity1Controller = Robolectric.buildActivity(StubActivity1::class.java)
    private val activity1 = activity1Controller.get()

    private val service1Controller = Robolectric.buildService(StubService1::class.java)
    private val service1 = service1Controller.get()

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
}

class StubApplication : Application()

private class StubActivity1 : ComponentActivity()

private class StubService1 : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
