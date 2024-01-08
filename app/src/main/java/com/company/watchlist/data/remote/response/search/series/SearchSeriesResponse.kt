package com.company.watchlist.data.remote.response.search.series

data class SearchSeriesResponse(
    val page: Int,
    val results: List<SearchSeriesResult>,
    val total_pages: Int,
    val total_results: Int
)