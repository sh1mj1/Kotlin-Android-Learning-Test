package com.example.learningtest.sealedclass

import androidx.lifecycle.ViewModel
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("String UiState: load Ui")
class TestSealedClassUiState : FreeSpec({
    "MyViewModel should emit Loading and Success states" {
        runTest {
            val viewModel = StubViewModel(coroutineScope = this)
            val emittedStates = mutableListOf<UiState>()
            val job =
                launch {
                    viewModel.uiState.toList(emittedStates)
                }

            viewModel.loadData()

            advanceTimeBy(2000)
            emittedStates[0] shouldBe UiState.Loading
            emittedStates[1] shouldBe UiState.Success("Hello, Android!")

            job.cancel()
        }
    }

    "MyViewModel should emit Loading and Error states" {
        runTest {
            val viewModel = StubViewModel(coroutineScope = this)
            val emittedStates = mutableListOf<UiState>()
            val job =
                launch {
                    viewModel.uiState.toList(emittedStates)
                }

            viewModel.loadDataWithError()

            advanceTimeBy(2000)
            emittedStates[0] shouldBe UiState.Loading
            emittedStates[1] shouldBe UiState.Error("Failed to load data")

            job.cancel()
        }
    }
})

private sealed class UiState {
    data object Loading : UiState()

    data class Success(val data: String) : UiState()

    data class Error(val message: String) : UiState()
}

private class StubViewModel(
    private val coroutineScope: CoroutineScope,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState

    fun loadData() {
        _uiState.value = UiState.Loading

        coroutineScope.launch {
            delay(1000) // Simulate network delay
            val data = "Hello, Android!"
            _uiState.value = UiState.Success(data)
        }
    }

    fun loadDataWithError() {
        _uiState.value = UiState.Loading

        coroutineScope.launch {
            delay(1000) // Simulate network delay
            val errorMessage = "Failed to load data"
            _uiState.value = UiState.Error(errorMessage)
        }
    }
}
