package com.company.watchlist.data.mappers

import com.company.watchlist.data.local.trending.TrendingEntity
import com.company.watchlist.data.remote.response.trending.TrendingResult

fun TrendingResult.toTrendingEntity(): TrendingEntity{
    return TrendingEntity(
        adult = adult,
        backdrop_path = backdrop_path,
        first_air_date = first_air_date,
        genre_ids = genre_ids,
        id = id,
        media_type = media_type,
        name = name,
        original_language = original_language,
        original_name = original_name,
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

fun TrendingEntity.toTrendingResult(): TrendingResult{
    return TrendingResult(
        adult = adult,
        backdrop_path = backdrop_path,
        first_air_date = first_air_date,
        genre_ids = genre_ids,
        id = id,
        media_type = media_type,
        name = name,
        original_language = original_language,
        original_name = original_name,
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