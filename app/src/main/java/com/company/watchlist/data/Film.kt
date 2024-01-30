package com.company.watchlist.data

data class Film(
    val id: Long,
    val name: String,
    val listType: ListType,
    val averageRating: Double?,
    val posterPath: String?
)
