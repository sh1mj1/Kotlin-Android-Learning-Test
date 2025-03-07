package com.example.learningtest.solid.pure

/**
 * Model - 로또 판매 시스템
 */
data class Lottery(
    val numbers: List<Int>,
    val shape: Shape,
) {
    init {
        numbers.forEach {
            require(numbers.size == NUMBER_COUNT) { INVALID_COUNT_MESSAGE }
            require(numbers.toSet().size == NUMBER_COUNT) { DUPLICATE_NUMBER_MESSAGE }
            require(it in MIN_NUMBER..MAX_NUMBER) { INVALID_NUMBER_MESSAGE }
        }
    }

    companion object {
        private const val MIN_NUMBER = 1
        private const val MAX_NUMBER = 45
        val numberRange = (MIN_NUMBER..MAX_NUMBER)
        const val NUMBER_COUNT = 6

        private const val INVALID_COUNT_MESSAGE = "Invalid lotto number count"
        private const val DUPLICATE_NUMBER_MESSAGE = "Duplicate lotto number"
        private const val INVALID_NUMBER_MESSAGE = "Invalid lotto number"

        fun create(
            numbers: List<Int>,
            shape: Shape,
        ): LottoResult<Lottery> =
            runCatching { Lottery(numbers, shape) }
                .fold(
                    onSuccess = { LottoResult.Success(it) },
                    onFailure = { e ->
                        when (e.message) {
                            INVALID_COUNT_MESSAGE -> LottoResult.Failure.InvalidCount
                            DUPLICATE_NUMBER_MESSAGE -> LottoResult.Failure.DuplicateNumber
                            INVALID_NUMBER_MESSAGE -> LottoResult.Failure.InvalidNumber
                            else -> throw e
                        }
                    },
                )
    }
}

sealed class LottoResult<out T> {
    data class Success<T>(val value: T) : LottoResult<T>()

    sealed class Failure : LottoResult<Nothing>() {
        data object InvalidCount : Failure()

        data object DuplicateNumber : Failure()

        data object InvalidNumber : Failure()
    }
}
