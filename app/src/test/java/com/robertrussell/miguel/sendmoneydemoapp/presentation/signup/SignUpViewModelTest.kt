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
        viewModel.onNameChange("John Doe")
        assertEquals("John Doe", viewModel.name)
    }

    @Test
    fun `onEmailChange with valid email sets isEmailValid true`() {
        viewModel.onEmailChange("test@example.com")
        assertTrue(viewModel.isEmailValid)
    }

    @Test
    fun `onEmailChange with invalid email sets isEmailValid false`() {
        viewModel.onEmailChange("invalid-email")
        assertFalse(viewModel.isEmailValid)
    }

    @Test
    fun `onPasswordChange with short password sets isPasswordValid false`() {
        viewModel.onPasswordChange("123")
        assertFalse(viewModel.isPasswordValid)
    }

    @Test
    fun `onPasswordChange with long password sets isPasswordValid true`() {
        viewModel.onPasswordChange("12345678")
        assertTrue(viewModel.isPasswordValid)
    }

    @Test
    fun `signUp success calls onSuccess`() = runTest {
        viewModel.onNameChange("John")
        viewModel.onEmailChange("john@example.com")
        viewModel.onPasswordChange("password123")
        
        coEvery { signUpUseCase(any(), any(), any()) } returns Result.success(Unit)

        var successCalled = false
        viewModel.signUp(
            onSuccess = { successCalled = true },
            onError = {}
        )

        assertTrue(successCalled)
    }

    @Test
    fun `signUp failure calls onError`() = runTest {
        viewModel.onNameChange("John")
        viewModel.onEmailChange("john@example.com")
        viewModel.onPasswordChange("password123")
        
        val errorMsg = "Email already exists"
        coEvery { signUpUseCase(any(), any(), any()) } returns Result.failure(Exception(errorMsg))

        var error: String? = null
        viewModel.signUp(
            onSuccess = {},
            onError = { error = it }
        )

        assertEquals(errorMsg, error)
    }

    @Test
    fun `clearFields resets state`() {
        viewModel.onNameChange("John")
        viewModel.clearFields()
        assertEquals("", viewModel.name)
        assertTrue(viewModel.isEmailValid)
    }
}
