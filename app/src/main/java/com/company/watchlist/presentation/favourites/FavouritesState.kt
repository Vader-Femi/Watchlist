package com.company.watchlist.presentation.favourites

import com.company.watchlist.data.Film

data class FavouritesState(
    val favouritesMoviesList: List<Film> = emptyList(),
    val favouritesSeriesList: List<Film> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)