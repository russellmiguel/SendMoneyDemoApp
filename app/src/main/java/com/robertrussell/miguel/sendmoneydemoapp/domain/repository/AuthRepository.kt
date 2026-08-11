package com.robertrussell.miguel.sendmoneydemoapp.domain.repository

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(name: String, email: String, pass: String): Result<Unit>
    suspend fun login(email: String, pass: String): Result<User>
    fun observeBalance(email: String): Flow<Double>
}
