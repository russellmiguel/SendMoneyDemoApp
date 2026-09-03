package com.robertrussell.miguel.sendmoneydemoapp.presentation.signup

import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.SignUpUseCase
import com.robertrussell.miguel.sendmoneydemoapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val signUpUseCase: SignUpUseCase = mockk()
    private val viewModel = SignUpViewModel(signUpUseCase)

    @Test
    fun `onNameChange updates name`() {
        viewModel.onEvent(SignUpEvent.NameChanged("John Doe"))
        assertEquals("John Doe", viewModel.uiState.value.name)
    }

    @Test
    fun `onEmailChange with valid email sets isEmailValid true`() {
        viewModel.onEvent(SignUpEvent.EmailChanged("test@example.com"))
        assertTrue(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onEmailChange with invalid email sets isEmailValid false`() {
        viewModel.onEvent(SignUpEvent.EmailChanged("invalid-email"))
        assertFalse(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onPasswordChange with short password sets isPasswordValid false`() {
        viewModel.onEvent(SignUpEvent.PasswordChanged("123"))
        assertFalse(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onPasswordChange with long password sets isPasswordValid true`() {
        viewModel.onEvent(SignUpEvent.PasswordChanged("12345678"))
        assertTrue(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `signUp success sets status to Success`() = runTest {
        viewModel.onEvent(SignUpEvent.NameChanged("John"))
        viewModel.onEvent(SignUpEvent.EmailChanged("john@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        
        coEvery { signUpUseCase(any(), any(), any()) } returns Result.success(Unit)

        viewModel.onEvent(SignUpEvent.OnSignUp)

        assertEquals(SignUpStatus.Success, viewModel.uiState.value.status)
    }

    @Test
    fun `signUp failure sets status to Error`() = runTest {
        viewModel.onEvent(SignUpEvent.NameChanged("John"))
        viewModel.onEvent(SignUpEvent.EmailChanged("john@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("password123"))
        
        val errorMsg = "Email already exists"
        coEvery { signUpUseCase(any(), any(), any()) } returns Result.failure(Exception(errorMsg))

        viewModel.onEvent(SignUpEvent.OnSignUp)

        val status = viewModel.uiState.value.status
        assertTrue(status is SignUpStatus.Error)
        assertEquals(errorMsg, (status as SignUpStatus.Error).message)
    }

    @Test
    fun `clearFields resets state`() {
        viewModel.onEvent(SignUpEvent.NameChanged("John"))
        viewModel.onEvent(SignUpEvent.ClearFields)
        assertEquals("", viewModel.uiState.value.name)
        assertTrue(viewModel.uiState.value.isEmailValid)
    }
}
