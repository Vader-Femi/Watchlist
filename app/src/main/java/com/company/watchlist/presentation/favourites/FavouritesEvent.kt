package com.company.watchlist.presentation.favourites

import com.company.watchlist.data.Film

sealed class FavouritesEvent {
    data class IsLoadingChanged(val isLoading: Boolean): FavouritesEvent()
    data class RemoveFromFavourites(val film: Film): FavouritesEvent()
    data object GetList: FavouritesEvent()
    data object DismissError: FavouritesEvent()
}