package com.robertrussell.miguel.sendmoneydemoapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }

            is SignUpEvent.EmailChanged -> {
                val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(event.email)
                    .matches() || event.email.isEmpty()
                _uiState.update { it.copy(email = event.email, isEmailValid = isValid) }
            }

            is SignUpEvent.PasswordChanged -> {
                val isValid = event.password.length >= 8 || event.password.isEmpty()
                _uiState.update { it.copy(password = event.password, isPasswordValid = isValid) }
            }

            is SignUpEvent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            SignUpEvent.OnSignUp -> signUp()

            SignUpEvent.ClearFields -> {
                _uiState.update { SignUpUiState() }
            }
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(status = SignUpStatus.Idle) }
    }

    fun signUp() {
        val state = _uiState.value

        if (state.name.isEmpty() || state.email.isEmpty() || state.password.isEmpty()) {
            _uiState.update { it.copy(status = SignUpStatus.Error("Please fill in all fields")) }
            return
        }

        if (!state.isEmailValid) {
            _uiState.update { it.copy(status = SignUpStatus.Error("Please enter a valid email")) }
            return
        }

        if (!state.isPasswordValid || state.password.length < 8) {
            _uiState.update { it.copy(status = SignUpStatus.Error("Password must be at least 8 characters")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(status = SignUpStatus.Loading) }

            val result = signUpUseCase(state.name, state.email, state.password)

            _uiState.update { currentState ->
                currentState.copy(
                    status = result.fold(
                        onSuccess = { SignUpStatus.Success },
                        onFailure = {
                            SignUpStatus.Error(
                                result.exceptionOrNull()?.message ?: "Sign up failed"
                            )
                        }
                    )
                )
            }
        }
    }
}
