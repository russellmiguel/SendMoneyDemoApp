package com.robertrussell.miguel.sendmoneydemoapp.presentation.signup

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val isEmailValid: Boolean = true,
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val isPasswordVisible: Boolean = false,

    // SignUp status result
    val status: SignUpStatus = SignUpStatus.Idle
)

sealed interface SignUpStatus {
    object Idle : SignUpStatus
    object Loading : SignUpStatus
    object Success : SignUpStatus
    data class Error(val message: String) : SignUpStatus
}
