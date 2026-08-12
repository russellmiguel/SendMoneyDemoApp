package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTransactionsUseCaseTest {

    private lateinit var getTransactionsUseCase: GetTransactionsUseCase
    private val repository: TransactionRepository = mockk()

    @Before
    fun setUp() {
        getTransactionsUseCase = GetTransactionsUseCase(repository)
    }

    @Test
    fun `invoke should return transactions from repository`() = runBlocking {
        val email = "test@example.com"
        val transactions = listOf(
            Transaction(1, 100.0, "Recipient", 123456789L, "SEND")
        )
        every { repository.getTransactions(email) } returns flowOf(transactions)

        val result = getTransactionsUseCase(email)

        result.collect {
            assertEquals(transactions, it)
        }
    }

    @Test
    fun `getRemoteTransactions should return result from repository`() = runBlocking {
        val transactions = listOf(
            Transaction(1, 100.0, "Recipient", 123456789L, "SEND")
        )
        val expectedResult = Result.success(transactions)
        coEvery { repository.getRemoteTransactions() } returns expectedResult

        val result = getTransactionsUseCase.getRemoteTransactions()

        assertEquals(expectedResult, result)
    }
}
