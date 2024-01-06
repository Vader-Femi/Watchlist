package com.company.watchlist.data.remote.response.search.series

import com.company.watchlist.data.domain.search.series.SearchSeriesResult

data class SearchSeriesResponse(
    val page: Int,
    val results: List<SearchSeriesResult>,
    val total_pages: Int,
    val total_results: Int
)