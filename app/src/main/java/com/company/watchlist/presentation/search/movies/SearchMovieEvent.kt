package com.company.watchlist.presentation.search.movies

sealed class SearchMovieEvent {
    data class SearchQueryChanged(val query: String): SearchMovieEvent()

    object Search: SearchMovieEvent()
}