package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class AddBalanceUseCaseTest {
    private val repository: TransactionRepository = mockk()
    private val addBalanceUseCase = AddBalanceUseCase(repository)

    @Test
    fun `invoke should call repository addBalance`() = runBlocking {
        coEvery { repository.addBalance("test@example.com", 100.0) } returns Result.success(Unit)

        val result = addBalanceUseCase("test@example.com", 100.0)

        assertTrue(result.isSuccess)
    }
}
