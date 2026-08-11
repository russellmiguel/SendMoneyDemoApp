package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke(email: String): Flow<List<Transaction>> {
        return repository.getTransactions(email)
    }
}
