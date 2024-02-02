package com.company.watchlist.navigation

sealed class Screen(val route: String, var name: String, val smallText: String? = null) {

    data object AuthenticationRoute : Screen("authentication_route", "Authentication Route")
    data object OnBoardingScreen : Screen("on_boarding_screen", "OnBoarding")
    data object SignUpScreen : Screen("sign_up_screen", "Create your account", "Enter your details to get started")
    data object LogInScreen : Screen("log_in_screen", "Log In", "Enter your details to get started")
    data object ResetPasswordScreen : Screen("reset_password_screen", "Reset Password", "Enter email of the account to reset")


    data object WatchlistRoute : Screen("watchlist_route", "Watchlist Route")
    data object TrendingScreen : Screen("trending_screen", "Trending", "All the latest stuff")
    data object SearchScreen : Screen("search_screen", "Search", "What are you looking for?")
    data object SeriesDetailsScreen : Screen("series_details_screen", "Series")
    data object MovieDetailsScreen : Screen("movie_details_screen", "Movie")
    data object FavouritesScreen : Screen("favourites_screen", "Favourites", "Your personal watchlist")

    fun withArgs(vararg args: Pair<String, Int>): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("?${arg.first}=${arg.second}")
            }
        }
    }
}
