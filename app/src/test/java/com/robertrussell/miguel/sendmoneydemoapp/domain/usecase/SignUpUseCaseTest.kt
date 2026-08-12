package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SignUpUseCaseTest {
    private val repository: AuthRepository = mockk()
    private val signUpUseCase = SignUpUseCase(repository)

    @Test
    fun `invoke should call repository signUp`() = runBlocking {
        coEvery { repository.signUp("Name", "test@example.com", "pass") } returns Result.success(Unit)

        val result = signUpUseCase("Name", "test@example.com", "pass")

        assertTrue(result.isSuccess)
    }
}
