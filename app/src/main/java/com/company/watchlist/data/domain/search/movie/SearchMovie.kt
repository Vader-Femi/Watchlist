package com.company.watchlist.data.domain.search.movie
data class SearchMovie(
    val page: Int,
    val results: List<SearchMovieResult>,
    val total_pages: Int,
    val total_results: Int
)