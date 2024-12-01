package com.example.learningtest.sealedclass

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

@DisplayName("UserService: communicate with Backend server")
class TestSealedClassUserService : BehaviorSpec({
    Given("communicate with Backend Server: UserService") {
        val userService = UserService()
        When("when fetching a valid user") {
            Then("should emit Loading and then Success with the user data") {
                runTest {
                    val results = userService.user(0).toList()

                    results[0] shouldBe NetworkResult.Loading
                    results[1] shouldBe NetworkResult.Success(User("Alice", 20))
                }
            }
        }
        When("when fetching an invalid user") {
            Then("should emit Loading and then Error") {
                runTest {
                    val results = userService.user(2).toList()

                    results[0] shouldBe NetworkResult.Loading
                    results[1].shouldBeInstanceOf<NetworkResult.Error>()

                    val error = results[1] as NetworkResult.Error
                    error.exception.shouldBeInstanceOf<IndexOutOfBoundsException>()
                }
            }
        }
    }
})

private sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()

    data class Error(val exception: Throwable) : NetworkResult<Nothing>()

    data object Loading : NetworkResult<Nothing>()
}

private class UserService {
    fun user(id: Int): Flow<NetworkResult<User>> =
        flow {
            emit(NetworkResult.Loading)
            try {
                delay(1000)
                val user = users.users[id]
                emit(NetworkResult.Success(user))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e))
            }
        }

    companion object {
        private val users =
            Users(
                User("Alice", 20),
                User("Bob", 25),
            )
    }
}

private data class User(val name: String, val age: Int)

private data class Users(val users: List<User>) {
    constructor(vararg users: User) : this(users.toList())
}
