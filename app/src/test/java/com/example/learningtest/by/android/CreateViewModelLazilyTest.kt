package com.example.learningtest.by.android

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CreateViewModelLazilyTest {
    @Test
    fun `viewmodel is initialized lazily, no need to initialize the viewmodel manually`() {
        val controller = Robolectric.buildActivity(StubActivity::class.java)
        val activity = controller.get()

        shouldNotThrow<UninitializedPropertyAccessException> {
            activity.viewModelByLazy.updateData("New Data")
            activity.viewModelByViewModels.updateData("New Data")
        }

        activity.viewModelByLazy.data shouldBe "New Data"
        activity.viewModelByViewModels.data shouldBe "New Data"
    }
}

private class StubActivity : ComponentActivity() {
    val viewModelByLazy: StubViewModelByLazy by lazy {
        ViewModelProvider(this)[StubViewModelByLazy::class.java]
    }

    /**
     * `by viewModels()` is a property delegate that creates a ViewModel instance scoped to the Activity.
     *  Reduces boilerplate code compared to manually initializing the ViewModel using ViewModelProvider.
     */
    val viewModelByViewModels: StubViewModelByViewModels by viewModels()
}

class StubViewModelByLazy : ViewModel() {
    var data: String = "Initial Data"

    fun updateData(newData: String) {
        data = newData
    }
}

class StubViewModelByViewModels : ViewModel() {
    var data: String = "Initial Data"

    fun updateData(newData: String) {
        data = newData
    }
}
