package com.robertrussell.miguel.sendmoneydemoapp.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class TransactionRequest(
    val title: String,
    val body: String,
    val userId: Int
)

data class TransactionResponse(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)

interface JsonPlaceholderApi {
    @POST("posts")
    suspend fun sendMoney(@Body request: TransactionRequest): Response<TransactionResponse>

    @POST("posts")
    suspend fun addBalance(@Body request: TransactionRequest): Response<TransactionResponse>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}
