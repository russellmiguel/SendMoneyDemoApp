package com.robertrussell.miguel.sendmoneydemoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT balance FROM users WHERE email = :email")
    fun observeUserBalance(email: String): Flow<Double?>

    @Query("UPDATE users SET balance = balance + :amount WHERE email = :email")
    suspend fun updateBalance(email: String, amount: Double)
}
