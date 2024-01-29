package com.company.watchlist.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.company.watchlist.navigation.Screen

object BottomNavBarData {
    fun getItems(): List<BottomBarItem> {
        return listOf(
            BottomBarItem(
                name = "Trending",
                route = Screen.TrendingScreen.route,
                icon = Icons.Default.Star
            ),
            BottomBarItem(
                name = "Search",
                route = Screen.SearchScreen.route,
                icon = Icons.Default.Search
            ),
            BottomBarItem(
                name = "Watchlist",
                route = Screen.FavouritesScreen.route,
                icon = Icons.Default.Favorite
            ))
    }

    data class BottomBarItem(
        val name: String,
        val route: String,
        val icon: ImageVector
    )
}