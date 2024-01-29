package com.company.watchlist.data

import com.company.watchlist.R

object OnBoardingData {
    fun getItems(): List<OnBoardingItem> {
        return listOf(
            OnBoardingItem(
                R.drawable.telecommuting_rafiki,
                "Loren Ipson",
                "Just some random words, iono what to put yet"
            ),
            OnBoardingItem(
                R.drawable.telecommuting_rafiki,
                "Loren Ipson",
                "Just some more random words, iono what to put yet"
            ),
            OnBoardingItem(
                R.drawable.telecommuting_rafiki,
                "Loren Ipson",
                "Some more final random words, iono what to put yet"
            )
        )
    }

    data class OnBoardingItem(
        val image: Int,
        val title: String,
        val description: String,
    )
}