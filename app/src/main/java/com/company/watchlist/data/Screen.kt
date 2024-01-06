package com.company.watchlist.data

sealed class Screen(val route: String, val name: String) {

    object NavigationRoute : Screen("navigation_route", "")
    object TrendingScreen : Screen("trending_screen", "Trending")
    object SearchScreen : Screen("search_screen", "Search")
    object SeriesDetailsScreen : Screen("series_details_screen", "Series Details")
    object MovieDetailsScreen : Screen("movie_details_screen", "Movie Details")
    object WatchlistScreen : Screen("watchlist_screen", "Watchlist")

    fun withArgs(vararg args: Pair<String, String>): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("?${arg.first}=${arg.second}")
            }
        }
    }
}
