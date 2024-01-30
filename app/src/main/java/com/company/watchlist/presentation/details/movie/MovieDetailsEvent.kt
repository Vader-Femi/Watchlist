package com.company.watchlist.presentation.details.movie

sealed class MovieDetailsEvent {
    data class SetId(val id: Long): MovieDetailsEvent()
    data object GetDetails: MovieDetailsEvent()
    data object AddToFavourites: MovieDetailsEvent()
    data object DismissError: MovieDetailsEvent()
}