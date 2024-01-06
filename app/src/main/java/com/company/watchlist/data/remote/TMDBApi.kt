package com.company.watchlist.data.remote

import com.company.watchlist.BuildConfig
import com.company.watchlist.data.domain.details.movie.MovieDetails
import com.company.watchlist.data.domain.details.series.SeriesDetails
import com.company.watchlist.data.domain.search.movie.SearchMovie
import com.company.watchlist.data.domain.search.series.SearchSeries
import com.company.watchlist.data.domain.trending.Trending
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("/3/trending/all/week?language=en-UK")
    suspend fun getTrending(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): Trending

    @GET("/3/search/movie?")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("include_adult") includeAdult: String = "true",
        @Query("language") language: String = "en-UK",
        @Query("page") page: Int,
    ): SearchMovie

    @GET("/3/search/tv?")
    suspend fun searchSeries(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("include_adult") includeAdult: String = "true",
        @Query("language") language: String = "en-UK",
        @Query("page") page: Int,
    ): SearchSeries

    @GET("/3/movie/{movie_id}?")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") query: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-UK",
    ): MovieDetails

    @GET("/3/tv/{series_id}?")
    suspend fun getSeriesDetails(
        @Path("series_id") seriesId: Int,
        @Query("api_key") query: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-UK",
    ): SeriesDetails

}