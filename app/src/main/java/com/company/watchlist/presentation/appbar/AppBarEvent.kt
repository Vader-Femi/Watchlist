package com.company.watchlist.presentation.appbar

import com.company.watchlist.navigation.Screen

sealed class AppBarEvent {
    data class AppbarChanged(val screen: Screen): AppBarEvent()
}