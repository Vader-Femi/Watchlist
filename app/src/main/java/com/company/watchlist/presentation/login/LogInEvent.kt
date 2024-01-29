package com.company.watchlist.presentation.login

sealed class LogInEvent{
    data class EmailChanged(val email: String): LogInEvent()
    data class PasswordChanged(val password: String): LogInEvent()
    data class ShowPasswordChanged(val showPassword: Boolean): LogInEvent()

    object Submit: LogInEvent()
    object LogInSuccessful: LogInEvent()
}
