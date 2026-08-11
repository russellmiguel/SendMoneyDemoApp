package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository

class GetTransactionsUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(email: String): List<Transaction> {
        return repository.getTransactions(email)
    }
}
