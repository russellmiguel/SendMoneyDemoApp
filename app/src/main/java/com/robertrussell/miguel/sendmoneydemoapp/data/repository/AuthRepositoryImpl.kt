package com.robertrussell.miguel.sendmoneydemoapp.data.repository

import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserEntity
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.security.PasswordHasher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val passwordHasher: PasswordHasher
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, pass: String): Result<Unit> {
        return try {
            val passwordHash = passwordHasher.hash(pass)
            val user = UserEntity(
                email = email,
                name = name,
                passwordHash = passwordHash,
                balance = 0.0
            )
            userDao.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val user = userDao.getUserByEmail(email)
            if (user != null) {
                if (passwordHasher.verify(pass, user.passwordHash)) {
                    Result.success(User(email = user.email, name = user.name))
                } else {
                    Result.failure(Exception("Invalid credentials"))
                }
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeBalance(email: String): Flow<Double> {
        return userDao.observeUserBalance(email).map { it ?: 0.0 }
    }
}
