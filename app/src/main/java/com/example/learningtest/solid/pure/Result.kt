package com.example.learningtest.solid.pure

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()

    data class Failure(val reason: FailureReason) : Result<Nothing>()
}

sealed class FailureReason
