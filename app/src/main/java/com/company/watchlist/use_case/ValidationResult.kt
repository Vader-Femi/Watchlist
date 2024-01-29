package com.company.watchlist.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)