package com.example.learningtest.lateinit

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

/**
 * Currently, only JVM tests are officially supported in Kotest.
 *
 * We are open to suggestions on how to support UI tests.
 *
 * [https://kotest.io/docs/quickstart](https://kotest.io/docs/quickstart)
 */
@RunWith(RobolectricTestRunner::class)
class LateinitAndroidComponentTest {
    @Test
    fun `view widgets should be initialized after onCreate`() {
        val controller = Robolectric.buildActivity(StubActivity::class.java)
        val activity = controller.get()

        shouldThrow<UninitializedPropertyAccessException> {
            activity.textView.text
        }

        controller.create()

        activity.textView.text shouldBe ""
    }

    @Test
    fun `viewmodel should be initialized after onCreate`() {
        val controller = Robolectric.buildActivity(StubActivity::class.java)
        val activity = controller.get()

        shouldThrow<UninitializedPropertyAccessException> {
            activity.viewModel.updateData("New Data")
        }

        controller.create()

        activity.viewModel.data shouldBe "Initial Data"
    }
}

private class StubActivity : ComponentActivity() {
    lateinit var textView: TextView
    lateinit var viewModel: StubViewModelLateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textView = TextView(this) // `findViewById(R.id.textView)` in production
        viewModel = ViewModelProvider(this)[StubViewModelLateinit::class.java]
    }
}

class StubViewModelLateinit : ViewModel() {
    var data: String = "Initial Data"

    fun updateData(newData: String) {
        data = newData
    }
}
