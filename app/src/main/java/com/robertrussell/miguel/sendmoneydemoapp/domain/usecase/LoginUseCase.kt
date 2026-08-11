package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, pass: String): Result<User> {
        return repository.login(email, pass)
    }
}
