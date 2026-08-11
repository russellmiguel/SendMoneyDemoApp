package com.robertrussell.miguel.sendmoneydemoapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun login(onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())
            } else {
                onError(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }
}
