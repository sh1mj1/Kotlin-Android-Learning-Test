package lottery.pure

import kotlin.fold
import kotlin.runCatching

sealed interface Shape {
    fun area(): Int
}

data class Rectangle(var width: Int, var height: Int) : Shape {
    init {
        require(width > 0 && height > 0) { INVALID_SHAPE_SIZE }
    }

//    fun setWidth(width: Int) {
//        this.width = width
//    }
//
//    fun setHeight(height: Int) {
//        this.height = height
//    }

    override fun area() = width * height

    companion object {
        private const val INVALID_SHAPE_SIZE = "Invalid shape size"

        fun create(
            width: Int,
            height: Int,
        ): ShapeResult<Rectangle> =
            runCatching { Rectangle(width, height) }
                .fold(
                    onSuccess = { ShapeResult.Success(it) },
                    onFailure = { ShapeResult.Failure.InvalidShapeSize },
                )
    }
}

data class Square(var side: Int) : Shape {
    init {
        require(side > 0) {
            "Invalid side"
        }
    }

//    fun setSide(side: Int) {
//        this.side = side
//    }

    override fun area() = side * side

    companion object {
        fun create(side: Int): ShapeResult<Square> =
            runCatching { Square(side) }
                .fold(
                    onSuccess = { ShapeResult.Success(it) },
                    onFailure = { ShapeResult.Failure.InvalidShapeSize },
                )
    }
}

sealed class ShapeResult<out T> {
    data class Success<T>(val value: T) : ShapeResult<T>()

    sealed class Failure : ShapeResult<Nothing>() {
        data object InvalidShapeSize : Failure()
    }
}
