package com.company.watchlist.presentation.resetpassword

data class ResetPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val loadingError: String? = null
)
