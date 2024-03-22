package com.company.watchlist.presentation.trending

import androidx.compose.runtime.Immutable
import com.company.watchlist.data.remote.response.trending.TrendingResult

@Immutable
data class TrendingState(
    val trendingList: List<TrendingResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)