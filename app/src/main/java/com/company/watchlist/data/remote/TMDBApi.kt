package com.company.watchlist.data.remote

import com.company.watchlist.data.remote.response.search.movie.SearchMovieResponse
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResponse
import com.company.watchlist.data.remote.response.trending.TrendingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {

    @GET("/trending/all/week?language=en-UK")
    suspend fun getTrending(): TrendingResponse

    @GET("/search/movie?")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: String = "true",
        @Query("language") language: String = "en-UK",
        @Query("page") page: Int,
    ): SearchMovieResponse

    @GET("/search/tv?")
    suspend fun searchSeries(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: String = "true",
        @Query("language") language: String = "en-UK",
        @Query("page") page: Int,
    ): SearchSeriesResponse

    @GET("/movie?")
    suspend fun getMovieDetails(
        @Query("movie_id") movieId: Int,
        @Query("language") language: String = "en-UK",
    ): SearchSeriesResponse

    @GET("/tv?")
    suspend fun getSeriesDetails(
        @Query("series_id") seriesId: Int,
        @Query("language") language: String = "en-UK",
    ): SearchSeriesResponse

}