package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String): Flow<Double> {
        return repository.observeBalance(email)
    }
}
