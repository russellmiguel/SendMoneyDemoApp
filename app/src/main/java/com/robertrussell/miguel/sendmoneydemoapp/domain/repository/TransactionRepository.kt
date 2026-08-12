package com.robertrussell.miguel.sendmoneydemoapp.domain.repository

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(email: String): Flow<List<Transaction>>
    suspend fun getRemoteTransactions(): Result<List<Transaction>>
    suspend fun addTransaction(email: String, amount: Double, recipient: String, type: String)
    suspend fun sendMoney(email: String, amount: Double, recipient: String): Result<Unit>
    suspend fun addBalance(email: String, amount: Double): Result<Unit>
}
