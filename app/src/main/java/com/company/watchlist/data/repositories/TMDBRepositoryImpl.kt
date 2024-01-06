package com.company.watchlist.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.company.watchlist.data.remote.PAGE_SIZE
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.remote.TMDBApi
import com.company.watchlist.data.remote.pagination.search.movie.SearchMoviePagingSource
import com.company.watchlist.data.remote.pagination.search.series.SearchSeriesPagingSource
import com.company.watchlist.data.domain.search.movie.SearchMovieResult
import com.company.watchlist.data.domain.search.series.SearchSeriesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class TMDBRepositoryImpl @Inject constructor(
    private val api: TMDBApi
): TMDBRepository {

    override val dynamicColours = false

    override suspend fun <T> safeApiCall(
        apiCall: suspend () -> T,
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }

    override suspend fun getTrending() = safeApiCall {
        api.getTrending()
    }

    override fun searchMovies(query: String): Flow<PagingData<SearchMovieResult>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE)
        ){
            SearchMoviePagingSource(api, query)
        }.flow
    }

    override fun searchSeries(query: String): Flow<PagingData<SearchSeriesResult>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
        ){
            SearchSeriesPagingSource(api, query)
        }.flow
    }

    override suspend fun getMovieDetails(movieId: Int) = safeApiCall {
        api.getMovieDetails(movieId)
    }

    suspend fun getSeriesDetails(movieId: Int) = safeApiCall {
        api.getSeriesDetails(movieId)
    }

}