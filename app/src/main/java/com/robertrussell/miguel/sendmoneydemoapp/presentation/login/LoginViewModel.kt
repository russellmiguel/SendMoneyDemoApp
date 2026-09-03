package com.robertrussell.miguel.sendmoneydemoapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginUiState.update { it.copy(email = event.email) }
            }

            is LoginEvent.PasswordChanged -> {
                _loginUiState.update { it.copy(password = event.password) }
            }

            is LoginEvent.PasswordVisibleChanged -> {
                _loginUiState.update { it.copy(isPasswordVisible = event.passwordVisible) }
            }

            LoginEvent.OnLogin -> {
                val currentState = _loginUiState.value

                login(
                    password = currentState.password,
                    email = currentState.email
                )
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.update { it.copy(loginStatus = LoginStatus.Loading) }

            val result = loginUseCase(email, password)

            _loginUiState.update { state ->
                state.copy(
                    loginStatus = result.fold(
                        onSuccess = { LoginStatus.Success(it) },
                        onFailure = { LoginStatus.Error(it.message ?: "Login failed!") }
                    )
                )
            }
        }
    }

    fun resetStatus() {
        _loginUiState.update { it.copy(loginStatus = LoginStatus.Idle) }
    }
}
