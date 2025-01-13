package com.example.learningtest.compose.state

import androidx.compose.runtime.toMutableStateList

class WellnessViewModel {
    private val _tasks = wellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks

    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }
}
