package com.robertrussell.miguel.sendmoneydemoapp.data.repository

import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionEntity
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun getTransactions(email: String): List<Transaction> {
        return transactionDao.getTransactionsForUser(email).map {
            Transaction(
                id = it.id,
                amount = it.amount,
                recipient = it.recipient,
                date = it.date,
                type = it.type
            )
        }
    }

    override suspend fun addTransaction(email: String, amount: Double, recipient: String, type: String) {
        transactionDao.insertTransaction(
            TransactionEntity(
                userEmail = email,
                amount = amount,
                recipient = recipient,
                date = System.currentTimeMillis(),
                type = type
            )
        )
    }
}
