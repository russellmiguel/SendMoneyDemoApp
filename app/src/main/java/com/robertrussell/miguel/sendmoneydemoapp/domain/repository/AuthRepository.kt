package com.robertrussell.miguel.sendmoneydemoapp.domain.repository

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User

interface AuthRepository {
    suspend fun signUp(name: String, email: String, pass: String): Result<Unit>
    suspend fun login(email: String, pass: String): Result<User>
}
