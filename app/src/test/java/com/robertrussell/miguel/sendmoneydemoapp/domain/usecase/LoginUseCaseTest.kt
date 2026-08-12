package com.robertrussell.miguel.sendmoneydemoapp.domain.usecase

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUseCaseTest {
    private val repository: AuthRepository = mockk()
    private val loginUseCase = LoginUseCase(repository)

    @Test
    fun `invoke should call repository login`() = runBlocking {
        val user = User("test@example.com", "Name")
        coEvery { repository.login("test@example.com", "pass") } returns Result.success(user)

        val result = loginUseCase("test@example.com", "pass")

        assertEquals(user, result.getOrNull())
    }
}
