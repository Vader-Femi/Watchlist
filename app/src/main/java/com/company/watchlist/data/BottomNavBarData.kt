package com.company.watchlist.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.FavoriteBorder
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material.icons.twotone.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.company.watchlist.navigation.Screen

@Immutable
object BottomNavBarData {
    fun getItems(): List<BottomBarItem> {
        return listOf(
            BottomBarItem(
                name = "Trending",
                route = Screen.TrendingScreen.route,
                icon = Icons.TwoTone.Star
            ),
            BottomBarItem(
                name = "Search",
                route = Screen.SearchScreen.route,
                icon = Icons.TwoTone.Search
            ),
            BottomBarItem(
                name = "Favourite",
                route = Screen.FavouritesScreen.route,
                icon = Icons.TwoTone.FavoriteBorder
            ))
    }

    @Immutable
    data class BottomBarItem(
        val name: String,
        val route: String,
        val icon: ImageVector
    )
}