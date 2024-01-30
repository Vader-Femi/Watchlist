package com.company.watchlist.presentation.details.series

sealed class SeriesDetailsEvent {
    data class SetId(val id: Long): SeriesDetailsEvent()
    data object GetDetails: SeriesDetailsEvent()
    data object AddToFavourites: SeriesDetailsEvent()
    data object DismissError: SeriesDetailsEvent()
}