package com.company.watchlist.presentation.search.series

sealed class SearchSeriesEvent {
    data class SearchQueryChanged(val query: String): SearchSeriesEvent()

    object Search: SearchSeriesEvent()
}