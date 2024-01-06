package com.company.watchlist.presentation.search.movies

sealed class SearchMovieEvent {
    data class SearchQueryChanged(val query: String): SearchMovieEvent()
    data class ErrorChanged(val error: String?): SearchMovieEvent()
    data class Loading(val loading: Boolean): SearchMovieEvent()

    object Search: SearchMovieEvent()
}