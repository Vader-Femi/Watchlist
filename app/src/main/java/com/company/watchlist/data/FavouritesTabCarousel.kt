package com.company.watchlist.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

object FavouritesTabCarousel {
    fun getItems(): List<FavouritesTabItem> {
        return listOf(
            FavouritesTabItem(
                index = 0,
                name = "Movies",
                icon = Icons.Default.List
            ),
            FavouritesTabItem(
                index = 1,
                name = "Series",
                icon = Icons.Default.List
            )
        )
    }

    data class FavouritesTabItem(
        val index: Int,
        val name: String,
        val icon: ImageVector
    )
}