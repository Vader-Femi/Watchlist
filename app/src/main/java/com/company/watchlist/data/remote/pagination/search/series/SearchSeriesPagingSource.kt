package com.company.watchlist.data.remote.pagination.search.series

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.company.watchlist.data.remote.DEFAULT_PAGE_INDEX
import com.company.watchlist.data.remote.TMDBApi
import com.company.watchlist.data.domain.search.series.SearchSeriesResult
import retrofit2.HttpException
import java.io.IOException

class SearchSeriesPagingSource(private val tmdbApi: TMDBApi, private val query: String) :
    PagingSource<Int, SearchSeriesResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchSeriesResult> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = tmdbApi.searchSeries(query = query, page = page)
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

    override fun getRefreshKey(state: PagingState<Int, SearchSeriesResult>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}