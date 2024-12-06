package com.example.learningtest.nestedAndInner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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
