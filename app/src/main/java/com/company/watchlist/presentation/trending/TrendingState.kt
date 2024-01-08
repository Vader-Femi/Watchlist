package com.company.watchlist.presentation.trending

import com.company.watchlist.data.remote.response.trending.TrendingResult

data class TrendingState(
    val trendingList: List<TrendingResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)