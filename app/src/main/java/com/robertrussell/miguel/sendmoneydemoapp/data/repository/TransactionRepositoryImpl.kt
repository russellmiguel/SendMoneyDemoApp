package com.robertrussell.miguel.sendmoneydemoapp.data.repository

import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionEntity
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.data.remote.JsonPlaceholderApi
import com.robertrussell.miguel.sendmoneydemoapp.data.remote.TransactionRequest
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val userDao: UserDao,
    private val api: JsonPlaceholderApi
) : TransactionRepository {
    override fun getTransactions(email: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForUser(email).map { entities ->
            entities.map {
                Transaction(
                    id = it.id,
                    amount = it.amount,
                    recipient = it.recipient,
                    date = it.date,
                    type = it.type
                )
            }
        }
    }

    override suspend fun getRemoteTransactions(): Result<List<Transaction>> {
        return try {
            val response = api.getRemoteTransactions()
            if (response.isSuccessful) {
                val remoteTransactions = response.body() ?: emptyList()
                val transactions = remoteTransactions.map {
                    Transaction(
                        id = it.id,
                        amount = it.amount,
                        recipient = it.recipient,
                        date = it.date,
                        type = it.type
                    )
                }
                Result.success(transactions)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTransaction(
        email: String,
        amount: Double,
        recipient: String,
        type: String
    ) {
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

    override suspend fun sendMoney(
        email: String,
        amount: Double,
        recipient: String
    ): Result<Unit> {
        return try {
            val response = api.sendMoney(
                TransactionRequest(
                    title = "Money Sent",
                    body = "Sent $amount to $recipient",
                    userId = 1
                )
            )

            if (response.isSuccessful) {
                // Deduct balance locally
                userDao.updateBalance(email, -amount)

                // Save transaction locally
                addTransaction(email, amount, recipient, "SEND")

                Result.success(Unit)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addBalance(
        email: String,
        amount: Double
    ): Result<Unit> {
        return try {
            val response = api.addBalance(
                TransactionRequest(
                    title = "Deposit Money",
                    body = "Deposit $amount to balance.",
                    userId = 1
                )
            )

            if (response.isSuccessful) {
                // Update balance locally
                userDao.updateBalance(email, amount)

                // Save transaction locally
                addTransaction(email, amount, "Own Account", "DEPOSIT")

                Result.success(Unit)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
