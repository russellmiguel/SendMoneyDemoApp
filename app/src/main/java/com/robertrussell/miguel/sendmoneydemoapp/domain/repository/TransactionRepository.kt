package com.robertrussell.miguel.sendmoneydemoapp.domain.repository

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(email: String): List<Transaction>
    suspend fun addTransaction(email: String, amount: Double, recipient: String, type: String)
}
