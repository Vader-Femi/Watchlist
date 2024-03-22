package com.company.watchlist.data

import androidx.compose.runtime.Immutable

@Immutable
data class Film(
    val id: Long,
    val name: String,
    val listType: ListType,
    val averageRating: Double?,
    val posterPath: String?
)
