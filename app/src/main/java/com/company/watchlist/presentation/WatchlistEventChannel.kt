package com.company.watchlist.presentation

sealed class WatchlistEventChannel {
    data class AddedToFavourites(val addedToFavMessage: String): WatchlistEventChannel()
    data object LogUserOut: WatchlistEventChannel()
}