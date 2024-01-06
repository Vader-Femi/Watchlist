package com.company.watchlist.presentation.watchlist

data class WatchlistState(
    val watchlistList: List<Any>? = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)