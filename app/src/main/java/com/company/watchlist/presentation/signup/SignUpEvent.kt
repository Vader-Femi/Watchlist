package com.company.watchlist.presentation.signup

sealed class SignUpEvent{
    data class FirstNameChanged(val firstName: String): SignUpEvent()
    data class LastNameChanged(val lastName: String): SignUpEvent()
    data class EmailChanged(val email: String): SignUpEvent()
    data class PasswordChanged(val password: String): SignUpEvent()
    data class ShowPasswordChanged(val showPassword: Boolean): SignUpEvent()

    object Submit: SignUpEvent()
    object SignUpSuccessful: SignUpEvent()
}
