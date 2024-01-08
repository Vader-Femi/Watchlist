package com.company.watchlist.data.remote

import com.company.watchlist.BuildConfig
import com.company.watchlist.data.remote.response.details.movie.MovieDetailsResponse
import com.company.watchlist.data.remote.response.details.series.SeriesDetailsResponse
import com.company.watchlist.data.remote.response.search.movie.SearchMovieResponse
import com.company.watchlist.data.remote.response.search.series.SearchSeriesResponse
import com.company.watchlist.data.remote.response.trending.TrendingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("/3/trending/all/week?language=en-UK")
    suspend fun getTrending(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): TrendingResponse

    @GET("/3/search/movie?")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("include_adult") includeAdult: String = "true",
        @Query("language") language: String = "en-UK",
        @Query("page") page: Int,
    ): SearchMovieResponse

    @GET("/3/search/tv?")
    suspend fun searchSeries(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("include_adult") includeAdult: String = "true",
        @Query("language") language: String = "en-UK",
        @Query("page") page: Int,
    ): SearchSeriesResponse

    @GET("/3/movie/{movie_id}?")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") query: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-UK",
    ): MovieDetailsResponse

    @GET("/3/tv/{series_id}?")
    suspend fun getSeriesDetails(
        @Path("series_id") seriesId: Int,
        @Query("api_key") query: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-UK",
    ): SeriesDetailsResponse

}