package com.company.watchlist.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

object SearchTabCarousel {
    fun getItems(): List<SearchTabItem> {
        return listOf(
            SearchTabItem(
                index = 0,
                name = "Movies",
                icon = Icons.Default.List
            ),
            SearchTabItem(
                index = 1,
                name = "Series",
                icon = Icons.Default.List
            )
        )
    }

    data class SearchTabItem(
        val index: Int,
        val name: String,
        val icon: ImageVector
    )
}