package com.company.watchlist.data.remote.response.search.movie

import com.company.watchlist.data.domain.search.movie.SearchMovieResult

data class SearchMovieResponse(
    val page: Int,
    val results: List<SearchMovieResult>,
    val total_pages: Int,
    val total_results: Int
)