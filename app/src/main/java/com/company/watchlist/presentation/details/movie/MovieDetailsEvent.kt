package com.company.watchlist.presentation.details.movie

sealed class MovieDetailsEvent {
    data class SetId(val id: Int): MovieDetailsEvent()
    data object GetDetails: MovieDetailsEvent()
}