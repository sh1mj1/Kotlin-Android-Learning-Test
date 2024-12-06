package com.example.learningtest.nestedAndInner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * when you want to logically group related classes together but they don’t require direct access to the outer class’s instance.
 *
 * A common scenario is structuring UI states, event classes, or utility objects within a ViewModel or Activity class.
 *
 * This keeps the code more organized without introducing unnecessary coupling.
 *
 */
private class StubViewModel : ViewModel() {
    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State> get() = _state

    fun loadProduct() {
        viewModelScope.launch {
            try {
                val product = ProductRepo.product()
                _state.value = State.Content(product)
            } catch (e: Exception) {
                _state.value = State.Error("Failed to load product")
            }
        }
    }

    sealed class State {
        data object Loading : State()

        data class Content(val product: Product) : State()

        data class Error(val message: String) : State()
    }
}

private data class Product(val name: String)

private object ProductRepo {
    fun product() = Product("paper")
}
