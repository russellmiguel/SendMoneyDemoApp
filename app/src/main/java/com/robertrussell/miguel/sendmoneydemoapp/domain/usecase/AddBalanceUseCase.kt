package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import javax.inject.Inject

class AddBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(email: String, amount: Double): Result<Unit> {
        return repository.addBalance(email, amount)
    }
}
