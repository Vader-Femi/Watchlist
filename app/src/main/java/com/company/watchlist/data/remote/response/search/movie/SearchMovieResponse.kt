package com.company.watchlist.data.remote.response.search.movie

data class SearchMovieResponse(
    val page: Int,
    val results: List<SearchMovieResult>,
    val total_pages: Int,
    val total_results: Int
)