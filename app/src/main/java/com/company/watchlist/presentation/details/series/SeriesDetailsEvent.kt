package com.company.watchlist.presentation.details.series

sealed class SeriesDetailsEvent {
    data class SetId(val id: Int): SeriesDetailsEvent()
    data object GetDetails: SeriesDetailsEvent()
}