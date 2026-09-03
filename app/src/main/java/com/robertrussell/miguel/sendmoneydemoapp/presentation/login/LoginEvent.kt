package com.robertrussell.miguel.sendmoneydemoapp.presentation.login

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data class PasswordVisibleChanged(val passwordVisible: Boolean) : LoginEvent
    object OnLogin : LoginEvent
}