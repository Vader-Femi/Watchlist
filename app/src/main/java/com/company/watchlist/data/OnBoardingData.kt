package com.company.watchlist.data

import com.company.watchlist.R

object OnBoardingData {
    fun getItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem(
                R.drawable.movie_night_rafiki,
                "Stay in the Loop with Trending Movies",
                "Discover and catch the latest popular movies and shows"
            ),
            OnBoardingItem(
                R.drawable.research_paper_pana,
                "Search Anything You Love",
                "Find detailed info about any movie or show — all in one place"
            ),
            OnBoardingItem(
                R.drawable.todo_list_rafiki,
                "Build Your Watchlist",
                "Mark your favourites and keep track of what you love"
            )
        )
    }

    data class OnBoardingItem(
        val image: Int,
        val title: String,
        val description: String,
    )
}