package com.company.watchlist.presentation.trending

sealed class TrendingEvent {
    data object GetTrending: TrendingEvent()
    data object DismissError: TrendingEvent()
}