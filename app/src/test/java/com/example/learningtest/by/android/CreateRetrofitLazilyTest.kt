package com.example.learningtest.by.android

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class ByAndroidProductionTest : FreeSpec({
    "Retrofit" - {
        val server = MockWebServer()
        afterSpec {
            server.shutdown()
        }
        "RetrofitClient should initialize retrofit and apiService lazily" {
            server.enqueue(MockResponse().setBody("""[{"id":1,"name":"John Doe","email":"john@example.com"}]"""))
            server.start()
            val retrofitClient = RetrofitClient.created(server.url("/"))
            val apiService = retrofitClient.apiService

            runBlocking {
                val users = apiService.users()
                users.size shouldBe 1
                users[0].name shouldBe "John Doe"
            }

            server.shutdown()
        }
    }
})

/**
 * Ensures retrofit & apiService are only initialized when accessed.
 */
private class RetrofitClient private constructor(private val url: HttpUrl) {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    companion object {
        var instance: RetrofitClient? = null

        fun created(url: HttpUrl) = instance ?: RetrofitClient(url).also { instance = it }
    }
}

interface ApiService {
    @GET("users")
    suspend fun users(): List<User>

    data class User(val id: Int, val name: String, val email: String)
}
