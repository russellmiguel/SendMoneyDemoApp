package com.robertrussell.miguel.sendmoneydemoapp.presentation.signup

sealed interface SignUpEvent {
    data class NameChanged(val name: String) : SignUpEvent
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data class TogglePasswordVisibility(val toggle: Boolean) : SignUpEvent
    object OnSignUp : SignUpEvent
    object ClearFields : SignUpEvent
}