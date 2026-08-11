package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import javax.inject.Inject

class SendMoneyUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(email: String, amount: Double, recipient: String): Result<Unit> {
        return repository.sendMoney(email, amount, recipient)
    }
}
