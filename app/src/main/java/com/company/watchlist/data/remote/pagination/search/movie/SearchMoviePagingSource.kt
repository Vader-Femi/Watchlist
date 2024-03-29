package com.company.watchlist.data.remote.pagination.search.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.company.watchlist.data.DEFAULT_PAGE_INDEX
import com.company.watchlist.data.remote.TMDBApi
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import retrofit2.HttpException
import java.io.IOException

class SearchMoviePagingSource(private val tmdbApi: TMDBApi, private val query: String) :
    PagingSource<Int, SearchMovieResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMovieResult> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = tmdbApi.searchMovie(query = query, page = page)
            LoadResult.Page(
                response.results,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchMovieResult>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}