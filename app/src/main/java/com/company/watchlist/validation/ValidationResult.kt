package com.company.watchlist.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)