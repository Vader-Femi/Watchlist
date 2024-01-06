package com.company.watchlist.data.repositories

import androidx.paging.PagingData
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.domain.details.movie.MovieDetails
import com.company.watchlist.data.domain.search.movie.SearchMovieResult
import com.company.watchlist.data.domain.search.series.SearchSeriesResult
import com.company.watchlist.data.domain.trending.Trending
import kotlinx.coroutines.flow.Flow

interface TMDBRepository {

    val dynamicColours: Boolean
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T>
    suspend fun getTrending(): Resource<Trending>
    fun searchMovies(query: String): Flow<PagingData<SearchMovieResult>>
    fun searchSeries(query: String): Flow<PagingData<SearchSeriesResult>>
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails>
}