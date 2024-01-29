package com.company.watchlist.presentation.favourites

data class FavouritesState(
    val favouritesList: List<Any>? = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)