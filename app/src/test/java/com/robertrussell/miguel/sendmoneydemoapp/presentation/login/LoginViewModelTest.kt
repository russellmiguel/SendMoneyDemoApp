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
    fun `EmailChanged event updates email state`() {
        viewModel.onEvent(LoginEvent.EmailChanged("test@example.com"))
        assertEquals("test@example.com", viewModel.loginUiState.value.email)
    }

    @Test
    fun `PasswordChanged event updates password state`() {
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        assertEquals("password123", viewModel.loginUiState.value.password)
    }

    @Test
    fun `PasswordVisibleChanged event toggles state`() {
        val initial = viewModel.loginUiState.value.isPasswordVisible
        viewModel.onEvent(LoginEvent.PasswordVisibleChanged(!initial))
        assertNotEquals(initial, viewModel.loginUiState.value.isPasswordVisible)
        
        val updated = viewModel.loginUiState.value.isPasswordVisible
        viewModel.onEvent(LoginEvent.PasswordVisibleChanged(!updated))
        assertEquals(initial, viewModel.loginUiState.value.isPasswordVisible)
    }

    @Test
    fun `OnLogin event with success updates loginStatus to Success`() = runTest {
        val user = User("test@example.com", "Name")
        coEvery { loginUseCase(any(), any()) } returns Result.success(user)

        viewModel.onEvent(LoginEvent.OnLogin)

        assertTrue(viewModel.loginUiState.value.loginStatus is LoginStatus.Success)
        assertEquals(user, (viewModel.loginUiState.value.loginStatus as LoginStatus.Success).user)
    }

    @Test
    fun `OnLogin event with failure updates loginStatus to Error`() = runTest {
        val errorMessage = "Invalid credentials"
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception(errorMessage))

        viewModel.onEvent(LoginEvent.OnLogin)

        assertTrue(viewModel.loginUiState.value.loginStatus is LoginStatus.Error)
        assertEquals(errorMessage, (viewModel.loginUiState.value.loginStatus as LoginStatus.Error).message)
    }
}
