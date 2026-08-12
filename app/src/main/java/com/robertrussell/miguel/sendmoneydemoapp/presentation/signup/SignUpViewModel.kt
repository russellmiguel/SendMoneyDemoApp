package com.robertrussell.miguel.sendmoneydemoapp.presentation.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var isEmailValid by mutableStateOf(true)
        private set
    var password by mutableStateOf("")
        private set
    var isPasswordValid by mutableStateOf(true)
        private set
    var passwordVisible by mutableStateOf(false)
        private set

    fun onNameChange(newName: String) {
        name = newName
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches() || newEmail.isEmpty()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        isPasswordValid = newPassword.length >= 8 || newPassword.isEmpty()
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun clearFields() {
        name = ""
        email = ""
        password = ""
        isEmailValid = true
        isPasswordValid = true
        passwordVisible = false
    }

    fun signUp(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            onError("Please fill in all fields")
            return
        }

        if (!isEmailValid) {
            onError("Please enter a valid email")
            return
        }

        if (!isPasswordValid || password.length < 8) {
            isPasswordValid = false
            onError("Password must be at least 8 characters")
            return
        }

        viewModelScope.launch {
            val result = signUpUseCase(name, email, password)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }
}
