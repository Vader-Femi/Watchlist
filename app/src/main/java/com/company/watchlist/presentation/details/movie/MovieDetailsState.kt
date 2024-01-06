package com.company.watchlist.presentation.details.movie

import com.company.watchlist.data.domain.details.movie.Genre
import com.company.watchlist.data.domain.details.movie.ProductionCompany
import com.company.watchlist.data.domain.details.movie.SpokenLanguage

data class MovieDetailsState(
    val budget: Int = -1,
    val genres: List<Genre> = emptyList(),
    val homepage: String = "",
    val id: Int = -1,
    val original_language: String = "",
    val original_title: String = "",
    val title: String = "",
    val popularity: Double = 0.0,
    val production_companies: List<ProductionCompany> = emptyList(),
    val release_date: String = "",
    val revenue: Int = -1,
    val runtime: Int = -1,
    val spoken_languages: List<SpokenLanguage> = emptyList(),
    val status: String = "",
    val tagline: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)