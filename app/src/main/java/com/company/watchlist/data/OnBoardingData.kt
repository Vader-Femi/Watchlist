package com.company.watchlist.data

import com.company.watchlist.R

object OnBoardingData {
    fun getItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem(
                R.drawable.movie_night_rafiki,
                "Loren Ipson",
                "Just some random words, iono what to put yet"
            ),
            OnBoardingItem(
                R.drawable.research_paper_pana,
                "Loren Ipson",
                "Get vital details about interesting movies and tv series"
            ),
            OnBoardingItem(
                R.drawable.todo_list_rafiki,
                "Loren Ipson",
                "All your favourite movies and tv series in one place"
            )
        )
    }

    data class OnBoardingItem(
        val image: Int,
        val title: String,
        val description: String,
    )
}