package com.company.watchlist.data

import androidx.compose.runtime.Immutable

@Immutable
data class Profile(
    val email: String,
    val firstname: String,
    val lastname: String,
)
