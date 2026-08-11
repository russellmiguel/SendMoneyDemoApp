package com.robertrussell.miguel.sendmoneydemoapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val name: String,
    val passwordHash: String,
    val balance: Double
)
