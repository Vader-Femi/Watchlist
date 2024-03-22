package com.company.watchlist.presentation.favourites

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.company.watchlist.data.Film

@Immutable
data class FavouritesState(
    val favouritesMoviesList: List<Film> = emptyList(),
    val favouritesSeriesList: List<Film> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)