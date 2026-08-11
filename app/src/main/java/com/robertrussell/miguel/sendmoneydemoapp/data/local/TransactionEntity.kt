package com.robertrussell.miguel.sendmoneydemoapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val amount: Double,
    val recipient: String,
    val date: Long,
    val type: String // "SEND" or "RECEIVE"
)
