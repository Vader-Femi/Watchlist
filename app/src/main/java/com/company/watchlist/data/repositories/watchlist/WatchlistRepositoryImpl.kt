package com.company.watchlist.data.repositories.watchlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.company.watchlist.data.PAGE_SIZE
import com.company.watchlist.data.UserPreferences
import com.company.watchlist.data.remote.Resource
import com.company.watchlist.data.remote.TMDBApi
import com.company.watchlist.data.remote.pagination.search.movie.SearchMoviePagingSource
import com.company.watchlist.data.remote.pagination.search.series.SearchSeriesPagingSource
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResult
import com.company.watchlist.data.repositories.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class WatchlistRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    firebaseAuth: FirebaseAuth,
    firestoreReference: FirebaseFirestore,
    dataStore: UserPreferences
): WatchlistRepository, BaseRepositoryImpl(firebaseAuth, firestoreReference, dataStore){

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

    override suspend fun getMovieDetails(movieId: Long) = safeApiCall {
        api.getMovieDetails(movieId)
    }

    override suspend fun getSeriesDetails(movieId: Long) = safeApiCall {
        api.getSeriesDetails(movieId)
    }

}