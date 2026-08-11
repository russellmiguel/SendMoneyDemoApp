package com.robertrussell.miguel.sendmoneydemoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, TransactionEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val transactionDao: TransactionDao
}
