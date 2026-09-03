package com.robertrussell.miguel.sendmoneydemoapp.presentation.login

import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,

    // Login status result
    val loginStatus: LoginStatus = LoginStatus.Idle
    )

sealed interface LoginStatus {
    object Idle : LoginStatus
    object Loading : LoginStatus
    data class Success(val user: User) : LoginStatus
    data class Error(val message: String) : LoginStatus
}
