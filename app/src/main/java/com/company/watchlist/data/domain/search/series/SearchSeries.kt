package com.company.watchlist.data.domain.search.series

data class SearchSeries(
    val page: Int,
    val results: List<SearchSeriesResult>,
    val total_pages: Int,
    val total_results: Int
)