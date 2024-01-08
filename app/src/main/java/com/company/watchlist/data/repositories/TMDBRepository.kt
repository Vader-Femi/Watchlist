package com.company.watchlist.data.repositories

import androidx.paging.PagingData
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.remote.response.details.movie.MovieDetailsResponse
import com.company.watchlist.data.remote.response.details.series.SeriesDetailsResponse
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResult
import com.company.watchlist.data.remote.response.trending.TrendingResponse
import kotlinx.coroutines.flow.Flow

interface TMDBRepository {

    val dynamicColours: Boolean
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T>
    suspend fun getTrending(): Resource<TrendingResponse>
    fun searchMovies(query: String): Flow<PagingData<SearchMovieResult>>
    fun searchSeries(query: String): Flow<PagingData<SearchSeriesResult>>
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetailsResponse>
    suspend fun getSeriesDetails(movieId: Int): Resource<SeriesDetailsResponse>
}