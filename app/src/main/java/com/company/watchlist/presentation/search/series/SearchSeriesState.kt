package com.company.watchlist.presentation.search.series

import androidx.paging.PagingData
import com.company.watchlist.data.domain.search.series.SearchSeriesResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchSeriesState(
    val query: String = "",
    val searchError: String? = null,
    val isLoading: Boolean = false,
    val searchResult: Flow<PagingData<SearchSeriesResult>> = flow {  }
)