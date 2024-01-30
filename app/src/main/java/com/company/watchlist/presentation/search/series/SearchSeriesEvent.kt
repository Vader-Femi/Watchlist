package com.company.watchlist.presentation.search.series

import com.company.watchlist.presentation.search.movies.SearchMovieEvent

sealed class SearchSeriesEvent {
    data class SearchQueryChanged(val query: String): SearchSeriesEvent()
    data class ErrorChanged(val error: String?): SearchSeriesEvent()

    data class Loading(val loading: Boolean): SearchSeriesEvent()

    object Search: SearchSeriesEvent()
    object DismissError: SearchSeriesEvent()
}