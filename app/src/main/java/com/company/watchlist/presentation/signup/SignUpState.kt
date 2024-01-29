package com.company.watchlist.presentation.signup

data class SignUpState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val loadingError: String? = null
)
