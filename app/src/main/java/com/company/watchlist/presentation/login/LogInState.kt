package com.company.watchlist.presentation.login

data class LogInState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val loadingError: String? = null
)
