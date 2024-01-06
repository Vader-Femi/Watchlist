package com.company.watchlist.data.mappers

import com.company.watchlist.data.remote.response.search.movie.SearchMovieResult
import com.company.watchlist.data.domain.search.movie.SearchMovieResult as DomainSearchMovieResult

fun SearchMovieResult.toSearchMovieResult(): DomainSearchMovieResult {
    return DomainSearchMovieResult(

        adult = adult,
        backdrop_path = backdrop_path,
        genre_ids = genre_ids,
        id = id,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count
    )
}

fun DomainSearchMovieResult.toSearchMovieResult(): SearchMovieResult {
    return SearchMovieResult(

        adult = adult,
        backdrop_path = backdrop_path,
        genre_ids = genre_ids,
        id = id,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count
    )
}