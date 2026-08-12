package com.robertrussell.miguel.sendmoneydemoapp.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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

data class RemoteTransaction(
    val id: Int,
    val amount: Double,
    val recipient: String,
    val date: Long,
    val type: String
)

interface JsonPlaceholderApi {
    @POST("russellmiguel/demo/transactions")
    suspend fun sendMoney(@Body request: TransactionRequest): Response<TransactionResponse>

    @POST("russellmiguel/demo/transactions")
    suspend fun addBalance(@Body request: TransactionRequest): Response<TransactionResponse>

    @GET("russellmiguel/demo/transactions")
    suspend fun getRemoteTransactions(): Response<List<RemoteTransaction>>

    companion object {
        const val BASE_URL = "https://my-json-server.typicode.com/"
    }
}
