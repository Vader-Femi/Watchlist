package com.company.watchlist.presentation.appbar

sealed class AppBarEvent {
    data class AppbarTitleChanged(val title: String): AppBarEvent()
}