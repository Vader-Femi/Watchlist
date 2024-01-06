package com.company.watchlist.data.domain.trending

data class Trending(
    val page: Int,
    val results: List<TrendingResult>,
    val total_pages: Int,
    val total_results: Int
)