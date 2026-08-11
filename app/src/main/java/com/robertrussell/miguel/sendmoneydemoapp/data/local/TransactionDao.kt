package com.robertrussell.miguel.sendmoneydemoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE userEmail = :email ORDER BY date DESC")
    suspend fun getTransactionsForUser(email: String): List<TransactionEntity>
}
