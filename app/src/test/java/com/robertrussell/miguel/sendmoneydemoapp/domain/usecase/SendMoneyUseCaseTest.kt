package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SendMoneyUseCaseTest {
    private val repository: TransactionRepository = mockk()
    private val sendMoneyUseCase = SendMoneyUseCase(repository)

    @Test
    fun `invoke should call repository sendMoney`() = runBlocking {
        coEvery { repository.sendMoney("test@example.com", 100.0, "recipient") } returns Result.success(Unit)

        val result = sendMoneyUseCase("test@example.com", 100.0, "recipient")

        assertTrue(result.isSuccess)
    }
}
