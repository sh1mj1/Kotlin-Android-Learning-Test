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
            require(numbers.size == NUMBER_COUNT) {
                "Invalid lotto number count"
            }
            require(numbers.toSet().size == NUMBER_COUNT) {
                "Duplicate lotto number"
            }
            require(it in MIN_NUMBER..MAX_NUMBER) {
                "Invalid lotto number"
            }
        }
    }

    companion object {
        private const val MIN_NUMBER = 1
        private const val MAX_NUMBER = 45
        val numberRange = (MIN_NUMBER..MAX_NUMBER)
        const val NUMBER_COUNT = 6
    }
}

interface Shape {
    fun area(): Int
}

data class Rectangle(private var width: Int, private var height: Int) : Shape {
    init {
        require(width > 0 && height > 0) {
            "Width and height must be greater than 0"
        }
    }

    fun setWidth(width: Int) {
        this.width = width
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    override fun area() = width * height
}

data class Square(private var side: Int) : Shape {
    init {
        require(side > 0) {
            "Side must be greater than 0"
        }
    }

    fun setSide(side: Int) {
        this.side = side
    }

    override fun area() = side * side
}
