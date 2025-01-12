package com.example.learningtest.compose.state

data class WellnessTask(
    val id: Int,
    val label: String,
)

fun wellnessTasks() = List(30) { i -> WellnessTask(id = i, label = "Task # $i") }
