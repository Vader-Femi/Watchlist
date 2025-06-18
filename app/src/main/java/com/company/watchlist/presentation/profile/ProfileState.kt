package com.company.watchlist.presentation.profile

import androidx.compose.runtime.Immutable
import com.company.watchlist.data.Film
import com.company.watchlist.data.Profile

@Immutable
data class ProfileState(
    val profile: Profile = Profile("", "", ""),
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val isEditingProfile: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)