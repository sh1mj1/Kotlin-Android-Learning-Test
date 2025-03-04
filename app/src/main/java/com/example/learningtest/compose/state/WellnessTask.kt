package com.example.learningtest.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class WellnessTask(
    val id: Int,
    val label: String,
    initialChecked: Boolean = false,
) {
    var checked by mutableStateOf(initialChecked)
}

fun wellnessTasks() = List(30) { i -> WellnessTask(id = i, label = "Task # $i") }
