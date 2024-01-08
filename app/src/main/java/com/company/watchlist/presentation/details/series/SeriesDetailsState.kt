package com.company.watchlist.presentation.details.series

import com.company.watchlist.data.remote.response.details.series.Genre
import com.company.watchlist.data.remote.response.details.series.LastEpisodeToAir
import com.company.watchlist.data.remote.response.details.series.NextEpisodeToAir
import com.company.watchlist.data.remote.response.details.series.Season
import com.company.watchlist.data.remote.response.details.series.SpokenLanguage

data class SeriesDetailsState(
    val id: Int = -1,
    val firstAirDate: String = "",
    val lastAirDate: String = "",
    val homepage: String = "",
    val name: String = "",
    val originalName: String = "",
    val originalLanguage: String = "",
    val overview: String = "",
    val spokenLanguage: List<SpokenLanguage> = emptyList(),
    val tagline: String = "",
    val posterPath: String = "",
    val voteAverage: Double = -1.0,
    val numberOfSeasons: Int = -1,
    val numberOfEpisodes: Int = -1,
    val lastEpisodeToAir: LastEpisodeToAir? = null,
    val nextEpisodeToAir: NextEpisodeToAir? = null,
    val seasons: List<Season> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val languages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)