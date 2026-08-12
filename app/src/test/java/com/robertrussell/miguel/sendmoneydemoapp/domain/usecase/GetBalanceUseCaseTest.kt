package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetBalanceUseCaseTest {
    private val repository: AuthRepository = mockk()
    private val getBalanceUseCase = GetBalanceUseCase(repository)

    @Test
    fun `invoke should return balance from repository`() = runBlocking {
        every { repository.observeBalance("test@example.com") } returns flowOf(500.0)

        val result = getBalanceUseCase("test@example.com").first()

        assertEquals(500.0, result, 0.0)
    }
}
