package com.company.watchlist.presentation.search.movies

import androidx.paging.PagingData
import com.company.watchlist.data.domain.search.movie.SearchMovieResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchMovieState(
    val query: String = "",
    val searchError: String? = null,
    val isLoading: Boolean = false,
    val searchResult: Flow<PagingData<SearchMovieResult>> = flow {  }
)