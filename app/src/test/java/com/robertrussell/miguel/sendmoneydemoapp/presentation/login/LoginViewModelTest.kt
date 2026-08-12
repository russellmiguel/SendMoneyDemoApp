package com.robertrussell.miguel.sendmoneydemoapp.presentation.login

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.LoginUseCase
import com.robertrussell.miguel.sendmoneydemoapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val viewModel = LoginViewModel(loginUseCase)

    @Test
    fun `onEmailChange updates email state`() {
        viewModel.onEmailChange("test@example.com")
        assertEquals("test@example.com", viewModel.email)
    }

    @Test
    fun `onPasswordChange updates password state`() {
        viewModel.onPasswordChange("password123")
        assertEquals("password123", viewModel.password)
    }

    @Test
    fun `togglePasswordVisibility toggles state`() {
        val initial = viewModel.passwordVisible
        viewModel.togglePasswordVisibility()
        assertNotEquals(initial, viewModel.passwordVisible)
        viewModel.togglePasswordVisibility()
        assertEquals(initial, viewModel.passwordVisible)
    }

    @Test
    fun `login success calls onSuccess`() = runTest {
        val user = User("test@example.com", "Name")
        coEvery { loginUseCase(any(), any()) } returns Result.success(user)

        var successUser: User? = null
        viewModel.login(
            onSuccess = { successUser = it },
            onError = {}
        )

        assertEquals(user, successUser)
    }

    @Test
    fun `login failure calls onError`() = runTest {
        val errorMessage = "Invalid credentials"
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception(errorMessage))

        var error: String? = null
        viewModel.login(
            onSuccess = {},
            onError = { error = it }
        )

        assertEquals(errorMessage, error)
    }
}
